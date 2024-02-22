/*
 * Copyright 2022-2024 兮玥(190785909@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chestnut.cms.search.template.tag;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.chestnut.cms.search.es.doc.ESContent;
import com.chestnut.cms.search.vo.ESContentVO;
import com.chestnut.common.staticize.FreeMarkerUtils;
import com.chestnut.common.staticize.enums.TagAttrDataType;
import com.chestnut.common.staticize.tag.AbstractListTag;
import com.chestnut.common.staticize.tag.TagAttr;
import com.chestnut.common.staticize.tag.TagAttrOption;
import com.chestnut.common.utils.IdUtils;
import com.chestnut.common.utils.JacksonUtils;
import com.chestnut.common.utils.StringUtils;
import com.chestnut.exmodel.CmsExtendMetaModelType;
import com.chestnut.search.SearchConsts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CmsSearchContentTag extends AbstractListTag {

	public final static String TAG_NAME = "cms_search_content";

	public final static String NAME = "{FREEMARKER.TAG.NAME." + TAG_NAME + "}";
	public final static String DESC = "{FREEMARKER.TAG.DESC." + TAG_NAME + "}";
	private final static String ATTR_QUERY = "query";
	private final static String ATTR_CATALOG_ID = "catalogid";
	private final static String ATTR_CONTENT_TYPE = "contenttype";
	private final static String ATTR_MODE = "mode";

	private final ElasticsearchClient esClient;

	@Override
	public List<TagAttr> getTagAttrs() {
		List<TagAttr> tagAttrs = super.getTagAttrs();
		tagAttrs.add(new TagAttr(ATTR_QUERY, true, TagAttrDataType.STRING, "检索词"));
		tagAttrs.add(new TagAttr(ATTR_CATALOG_ID, false, TagAttrDataType.STRING, "栏目ID"));
		tagAttrs.add(new TagAttr(ATTR_CONTENT_TYPE, false, TagAttrDataType.STRING, "内容类型"));
		tagAttrs.add(new TagAttr(ATTR_MODE, false, TagAttrDataType.STRING, "检索方式",
				SearchMode.toTagAttrOptions(), SearchMode.FullText.name()));
		return tagAttrs;
	}

	@Override
	public TagPageData prepareData(Environment env, Map<String, String> attrs, boolean page, int size, int pageIndex) throws TemplateException {
		long siteId = FreeMarkerUtils.evalLongVariable(env, "Site.siteId");
		String mode = MapUtils.getString(attrs, ATTR_MODE, SearchMode.FullText.name());
		String query = MapUtils.getString(attrs, ATTR_QUERY);
		if (StringUtils.isEmpty(query)) {
			throw new TemplateException("Tag attr `query` cannot be empty.", env);
		}
		String contentType = MapUtils.getString(attrs, ATTR_CONTENT_TYPE);
		Long catalogId = MapUtils.getLong(attrs, ATTR_CATALOG_ID);
		try {
			SearchResponse<ObjectNode> sr = esClient.search(s -> {
				s.index(ESContent.INDEX_NAME) // 索引
					.query(q ->
						q.bool(b -> {
							b.must(must -> must.term(tq -> tq.field("siteId").value(siteId)));
							if (StringUtils.isNotEmpty(contentType)) {
								b.must(must -> must.term(tq -> tq.field("contentType").value(contentType)));
							}
							if (IdUtils.validate(catalogId)) {
								b.must(must -> must.term(tq -> tq.field("catalogId").value(catalogId)));
							}
							if (SearchMode.isFullText(mode)) {
								b.must(must -> must
									.multiMatch(match -> match
										.analyzer(SearchConsts.IKAnalyzeType_Smart)
										.fields("title^10", "fullText^1")
										.query(query)
									)
								);
							} else {
								b.must(should -> {
									String[] keywords = StringUtils.split(query, ",");
									for (String keyword : keywords) {
										should.constantScore(cs ->
												cs.boost(1F).filter(f ->
														f.match(m ->
																m.field("tags").query(keyword))));
									}
									return should;
								});
							}
							return b;
						})
					);
				if (SearchMode.isFullText(mode)) {
					s.highlight(h ->
							h.fields("title", f -> f.preTags("<font color='red'>").postTags("</font>"))
									.fields("fullText", f -> f.preTags("<font color='red'>").postTags("</font>")));
				}
				s.sort(sort -> sort.field(f -> f.field("_score").order(SortOrder.Desc)));
				s.sort(sort -> sort.field(f -> f.field("publishDate").order(SortOrder.Desc))); // 排序: _score:desc + publishDate:desc
				s.source(source -> source.filter(f -> f.excludes("fullText"))); // 过滤字段
				if (page) {
					s.from((pageIndex - 1) * size).size(size);  // 分页
				} else {
					s.from(0).size(size);  // 分页，0开始
				}
				return s;
			}, ObjectNode.class);
			List<ESContentVO> list = sr.hits().hits().stream().map(hit -> {
				ESContentVO vo = JacksonUtils.getObjectMapper().convertValue(hit.source(), ESContentVO.class);
				Objects.requireNonNull(hit.source()).fields().forEachRemaining(e -> {
					if (e.getKey().startsWith(CmsExtendMetaModelType.DATA_FIELD_PREFIX)) {
						String field = e.getKey().substring(CmsExtendMetaModelType.DATA_FIELD_PREFIX.length());
						String value = e.getValue().asText();
						vo.getExtendData().put(field, value);
					}
				});
				vo.setHitScore(hit.score());
				vo.setPublishDateInstance(LocalDateTime.ofEpochSecond(vo.getPublishDate(), 0, ZoneOffset.UTC));
				vo.setCreateTimeInstance(LocalDateTime.ofEpochSecond(vo.getCreateTime(), 0, ZoneOffset.UTC));
				if (SearchMode.isFullText(mode)) {
					hit.highlight().forEach((key, value) -> {
						try {
							if (key.equals("fullText")) {
								vo.setFullText(StringUtils.join(value.toArray(String[]::new)));
							} else if (key.equals("title")) {
								vo.setTitle(StringUtils.join(value.toArray(String[]::new)));
							}
						} catch (Exception ex) {
							log.warn("Search api row parse failed: ", ex);
						}
					});
				}
				return vo;
			}).toList();
			return TagPageData.of(list, page ? Objects.requireNonNull(sr.hits().total()).value() : list.size());
		} catch (IOException e) {
			throw new TemplateException(e, env);
		}
	}

	@Override
	public String getTagName() {
		return TAG_NAME;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESC;
	}

	private enum SearchMode {
		// 所有站点
		FullText("全文检索"),
		// 当前站点
		Tag("标签检索，多个标签英文逗号隔开");

		private final String desc;

		SearchMode(String desc) {
			this.desc = desc;
		}

		static boolean isFullText(String mode) {
			return FullText.name().equalsIgnoreCase(mode);
		}

		static boolean isTag(String mode) {
			return Tag.name().equalsIgnoreCase(mode);
		}

		static List<TagAttrOption> toTagAttrOptions() {
			return List.of(
					new TagAttrOption(FullText.name(), FullText.desc),
					new TagAttrOption(Tag.name(), Tag.desc)
			);
		}
	}
}
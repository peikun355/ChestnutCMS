package com.chestnut.block;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.chestnut.block.domain.vo.ManualPageWidgetVO;
import com.chestnut.common.utils.JacksonUtils;
import com.chestnut.common.utils.StringUtils;
import com.chestnut.contentcore.core.IPageWidget;
import com.chestnut.contentcore.core.IPageWidgetType;
import com.chestnut.contentcore.domain.CmsPageWidget;
import com.chestnut.contentcore.domain.vo.PageWidgetVO;
import com.chestnut.contentcore.util.InternalUrlUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 手动控制区块页面部件<br/>
 * 此区块内容支持多行多列自定义控制
 * 
 * @author 兮玥
 * @email 190785909@qq.com
 */
@RequiredArgsConstructor
@Component(IPageWidgetType.BEAN_NAME_PREFIX + ManualPageWidgetType.ID)
public class ManualPageWidgetType implements IPageWidgetType {

	public final static String ID = "manual";
	public final static String NAME = "{CMS.CONTENCORE.PAGEWIDGET." + ID + "}";
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getIcon() {
		return "el-icon-list";
	}
	
	@Override
	public String getRoute() {
		return "/cms/block/manual/editor";
	}

	@Override
	public IPageWidget loadPageWidget(CmsPageWidget cmsPageWdiget) {
		ManualPageWidget pw = new ManualPageWidget();
		pw.setPageWidgetEntity(cmsPageWdiget);
		return pw;
	}
	
	@Override
	public IPageWidget newInstance() {
		return new ManualPageWidget();
	}
	
	@Override
	public PageWidgetVO getPageWidgetVO(CmsPageWidget pageWidget) {
		ManualPageWidgetVO vo = new ManualPageWidgetVO();
		BeanUtils.copyProperties(pageWidget, vo);
		vo.setContent(this.parseContent(pageWidget, null, true));
		return vo;
	}
	
	@Override
	public List<RowData> parseContent(CmsPageWidget pageWidget, String publishPipeCode, boolean isPreview) {
		List<RowData> list = null;
		if (StringUtils.isNotEmpty(pageWidget.getContent())) {
			list = JacksonUtils.fromList(pageWidget.getContent(), RowData.class);
		}
		if (list == null) {
			list = List.of();
		}
		list.forEach(rd -> rd.getItems().forEach(item -> item.setLogoSrc(InternalUrlUtils.getActualPreviewUrl(item.logo))));
		return list;
	}
	

	@Getter
	@Setter
	public static class RowData {
		
		private List<ItemData> items;
	}

	@Getter
	@Setter
	public static class ItemData {
		
		private String title;
		
		private String titleStyle;
		
		private String summary;
		
		private String url;
		
		private String logo;
		
		private String logoSrc;
		
		private LocalDateTime publishDate;
	}
}

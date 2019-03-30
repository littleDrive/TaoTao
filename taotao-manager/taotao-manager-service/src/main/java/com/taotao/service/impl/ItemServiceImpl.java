package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.xmlrpc.base.Data;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired TbItemParamItemMapper itemParamItemMapper;
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//添加查询条件
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		//查询商品列表
				TbItemExample example = new TbItemExample();
				//分页处理
				PageHelper.startPage(page, rows);
				List<TbItem> list = itemMapper.selectByExample(example);
				//创建一个返回值对象
				EUDataGridResult result = new EUDataGridResult();
				result.setRows(list);
				//取记录总条数
				PageInfo<TbItem> pageInfo = new PageInfo<>(list);
				result.setTotal(pageInfo.getTotal());
				return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item, TbItemDesc itemDesc, String itemParams) {
		
		try {
//			生成商品ID
//			可以使用redis的自增长key,在没有使用redis之前使用IDUtils工具类
			Long itemId = IDUtils.genItemId();
//			不全不完整的字段
			item.setId(itemId);
			item.setCid((long)560);
			item.setStatus((byte)1);
			Date date = new Date();
			item.setCreated(date);
			item.setUpdated(date);
//			把数据插入到商品表
			itemMapper.insert(item);
//			添加商品描述
			itemDesc.setItemId(itemId);
			itemDesc.setCreated(date);
			itemDesc.setUpdated(date);
//			把商品数据插入到商品描述表
			itemDescMapper.insert(itemDesc);
//			添加规格参数
			insertItemParamItem(itemId, itemParams);
			
		} catch (Exception e) {

			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));	
		}
		
		return TaotaoResult.ok();
	}

	
	private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
		
//		创建一个pojo
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		Date data = new Date();
		itemParamItem.setCreated(data);
		itemParamItem.setUpdated(data);
		
//		向表中插入数据
		itemParamItemMapper.insert(itemParamItem);
		
		return TaotaoResult.ok();
		
		
	}

}

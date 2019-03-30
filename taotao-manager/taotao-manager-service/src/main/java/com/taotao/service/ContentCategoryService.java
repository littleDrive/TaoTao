package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TreeNode;

public interface ContentCategoryService {

	public List<TreeNode> getCategoryList(long parentId);
	
	public TaotaoResult insertContentCategory(long parentId, String name);
}

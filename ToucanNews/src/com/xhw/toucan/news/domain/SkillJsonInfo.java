package com.xhw.toucan.news.domain;

import java.util.List;

public class SkillJsonInfo
{
	public List<String> headImgList;
	
	public List<SkillInfo> skillList;
	
	public class SkillInfo
	{
		public String iconUrl;
		public String skillTitle;
		public int skillValue;
	}
}

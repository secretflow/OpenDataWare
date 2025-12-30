package com.cec.examine.util.name;
import java.util.List;
public class FamilyNames {

	private static String [] familyNames = {"赵","钱","孙","李","周","吴","郑","王","冯","陈"
			,"楮","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张","孔","曹","严"
			,"华","金","魏","陶","姜","戚","谢","邹","喻","柏","水","窦","章","云","苏","潘","葛"
			,"奚","范","彭","郎","鲁","韦","昌","马","苗","凤","花","方","俞","任","袁","柳","酆"
			,"鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝","邬"
			,"安","常","乐","于","时","傅","皮","卞","齐","康","伍","余","元","卜","顾","孟","平"
			,"黄","和","穆","萧","尹","姚","邵","湛","汪","祁","毛","禹","狄","米","贝","明","臧"
			,"计","伏","成","戴","谈","宋","茅","庞","熊","纪","舒","屈","项","祝","董","梁","杜"
			,"阮","蓝","闽","席","季","麻","强","贾","路","娄","危","江","童","颜","郭","梅","盛"
			,"林","刁","锺","徐","丘","骆","高","夏","蔡","田","樊","胡","凌","霍","虞","万","支"
			,"柯","昝","管","卢","莫","经","房","裘","缪","干","解","应","宗","丁","宣","贲","邓"
			,"郁","单","杭","洪","包","诸","左","石","崔","吉","钮","龚","程","嵇","邢","滑","裴"
			,"陆","荣","翁","荀","羊","於","惠","甄","麹","家","封","芮","羿","储","靳","汲","邴"
			,"糜","松","井","段","富","巫","乌","焦","巴","弓","牧","隗","山","谷","车","侯","宓"
			,"蓬","全","郗","班","仰","秋","仲","伊","宫","宁","仇","栾","暴","甘","斜","厉","戎"
			,"祖","武","符","刘","景","詹","束","龙","叶","幸","司","韶","郜","黎","蓟","薄","印"
			,"宿","白","怀","蒲","邰","从","鄂","索","咸","籍","赖","卓","蔺","屠","蒙","池","乔"
			,"阴","郁","胥","能","苍","双","闻","莘","党","翟","谭","贡","劳","逄","姬","申","扶"
			,"堵","冉","宰","郦","雍","郤","璩","桑","桂","濮","牛","寿","通","边","扈","燕","冀"
			,"郏","浦","尚","农","温","别","庄","晏","柴","瞿","阎","充","慕","连","茹","习","宦"
			,"艾","鱼","容","向","古","易","慎","戈","廖","庾","终","暨","居","衡","步","都","耿"
			,"满","弘","匡","国","文","寇","广","禄","阙","东","欧","殳","沃","利","蔚","越","夔"
			,"隆","师","巩","厍","聂","晁","勾","敖","融","冷","訾","辛","阚","那","简","饶","空"
			,"曾","毋","沙","乜","养","鞠","须","丰","巢","关","蒯","相","查","后","荆","红","游"
			,"竺","权","逑","盖","益","桓","公","上官","皇甫","令狐","诸葛","司徒","司马","申屠","夏侯","贺兰","完颜","慕容","尉迟","长孙"};
	private static String [] middle = 
		{"小","大","一","二","三","老","立","九","铁","万"
		,"建","文","国","凌","永","山","学","贤","君","浩"
		,"明","旭","天","海","子","乃","博","泊","白","洪"
		,"红","兰","也","米","丰","仁","惹","幺","交","方"
		,"圆","园","玉","石","早","免","日","句","长","杨"
		,"方","鸣","铭","茗","冥","昆","坤","申","醒","星"
		,"兴","幸","省","木","春","雨","进","近","金","垠"
		,"美","少","树","芳","毓","淑","宏","伟","庭","厅"
		,"婉","会","晓","慧","惠","汇","荟","回","辉","卉"
		,"力","丽","厉","广","洲","宇","智","知","之","直"};
	
	private static String [] last = 
		{"明","亮","华","兰","磊","玲","霞","奎","刚","芳"
		,"军","涛","辉","月","光","钢","缸","纲","冈","罡"
		,"桃","陶","韬","良","祥","翔","香","橡","杨","洋"
		,"阳","央","旭","煦","娟","玲","灵","全","泉","岭"
		,"果","珍","圆","源","媛","英","颖","鑫","淼","瑞"
		,"睿","蕊","芮","锐","蓉","融","山","艳","敏","斌"
		,"绚","轩","璇","旋","炫","勋","京","晶","韵","云"
		,"福","强","远","宇","伟","海","天","田","慧","荟"
		,"惠","峰","莉","慈","倍","沛","佩","培","柱","萍"
		,"猛","楠","M","煌","灿","生","升","胜","盛","谦"};
	
	private static String [] fake = {"一","二","三","四","五","六","七","八","九","十","百","千","万"};	
	
	private static List<String> englishSingleNameList;
	private static List<String> englishSpaceNameList;
	static {
		//englishSingleNameList = EnglishNameResource.getEnglishSingleNameTable();
		//englishSpaceNameList = EnglishNameResource.getEnglishSpaceNameTable();
	}
	
	public static String getFamilyName(String name) {
		return familyNames[Math.abs(name.hashCode()) % familyNames.length];
	}
	
	public static String getLikelyName(String name) {
		return middle[getStableHashCode(name) % middle.length ] + last[getStableHashCode(name) % last.length ];
	}
	
	public static String getFullName(String name) {
		return getFakeFullName(name);
	}
	/**
	 * 汉字名字
	 * @param name
	 * @return
	 */
	public static String getLikelyFullName(String name) {
		return getFamilyName(name) + getLikelyName(name);
	}
	
	public static String getFakeFullName(String name) {
		return getFamilyName(name) + fake[getStableHashCode(name) % fake.length] + fake[getStableHashCode(name) / 10 % fake.length];
	}
	
	public static String getFakeName(String name) {
		return fake[getStableHashCode(name) % fake.length] + fake[getStableHashCode(name) / 10 % fake.length];
	}
	
	public static String getEnglishSingleName(String name) {
		return englishSingleNameList.get(Math.abs(name.hashCode()) % englishSingleNameList.size());
	}
	
	public static String getEnglishSpaceName(String name) {
		return englishSpaceNameList.get(Math.abs(name.hashCode())% englishSpaceNameList.size());
	}
	
	/**
	 * 获取稳定的hashcode
	 * @param s
	 * @return
	 */
	private static int getStableHashCode(String s) {
		return s.length() > 1 ? Math.abs((s.substring(s.length()-2, s.length())).hashCode()): Math.abs(s.hashCode());
	}

	public static String [] getFamilyNames() {
		return familyNames;
	}
}

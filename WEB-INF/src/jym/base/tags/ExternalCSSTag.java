// CatfoOD 2009-10-20 обнГ10:19:42

package jym.base.tags;



public class ExternalCSSTag extends TagBase {
	
	public ExternalCSSTag(String csspath) {
		super("link", true);
		super.addAttribute("href", csspath);
		super.addAttribute("rel", "stylesheet");
		super.addAttribute("type", "text/css");
	}
	
}

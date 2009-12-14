// CatfoOD 2009-10-20 обнГ10:13:17

package jym.base.tags;


public class ExternalScriptTag extends TagBase {
	
	public ExternalScriptTag(String scriptpath) {
		super("script");
		this.addAttribute("language", "JavaScript");
		this.addAttribute("src", scriptpath);
		this.addAttribute("type", "text/javascript");
	}

}

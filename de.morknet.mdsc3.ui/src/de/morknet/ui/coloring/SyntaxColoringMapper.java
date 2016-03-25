package de.morknet.ui.coloring;

import org.eclipse.xtext.ide.editor.syntaxcoloring.HighlightingStyles;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;

public class SyntaxColoringMapper extends DefaultAntlrTokenToAttributeIdMapper
{
	@Override
	protected String calculateId(String tokenName, int tokenType)
	{
		if ("RULE_HEX".equals(tokenName) || "RULE_MAC".equals(tokenName) || "RULE_IPADDR".equals(tokenName))
		{
			return HighlightingStyles.NUMBER_ID;
		}
		return super.calculateId(tokenName, tokenType);
	}

}

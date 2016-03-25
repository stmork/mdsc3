package de.morknet.converter;

import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.nodemodel.INode;

public class MdscValueConverters extends DefaultTerminalConverters
{
	@ValueConverter(rule = "HEX")
	public IValueConverter<Integer> HEX()
	{
		return new AbstractNullSafeConverter<Integer>()
		{
			@Override
			protected String internalToString(Integer value)
			{
				return "0x" + Integer.toHexString(value);
			}

			@Override
			protected Integer internalToValue(String string, INode node)
			{
				if (string.startsWith("$"))
				{
					return Integer.valueOf(string.substring(1), 16);
				}
				else if (string.startsWith("0x"))
				{
					return Integer.valueOf(string.substring(2), 16);
				}
				return Integer.valueOf(string);
			}
		};
	}

	@ValueConverter(rule = "MAC")
	public IValueConverter<String> MAC()
	{
		return new AbstractNullSafeConverter<String>()
		{
			@Override
			protected String internalToString(String value)
			{
				return value.toLowerCase();
			}

			@Override
			protected String internalToValue(String string, INode node)
			{
				return string.toLowerCase();
			}
		};
	}

	@ValueConverter(rule = "INTERFACE")
	public IValueConverter<String> INTERFACE()
	{
		return new AbstractNullSafeConverter<String>()
		{
			@Override
			protected String internalToString(String value)
			{
				return value.toLowerCase();
			}

			@Override
			protected String internalToValue(String string, INode node)
			{
				if(string.startsWith("^") || string.startsWith("#"))
				{
					return string.substring(1);
				}
				return string;
			}
		};
	}

	@ValueConverter(rule = "HOSTNAME")
	public IValueConverter<String> HOSTNAME()
	{
		return new AbstractNullSafeConverter<String>()
		{
			@Override
			protected String internalToString(String value)
			{
				return GrammarUtil.getAllKeywords(getGrammar()).contains(value) ? "_" + value : value;
			}

			@Override
			protected String internalToValue(String string, INode node)
			{
				return string.startsWith("_") ? string.substring(1) : string;
			}
		};
	}
}

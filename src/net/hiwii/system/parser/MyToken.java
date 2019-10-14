package net.hiwii.system.parser;

public class MyToken extends Token
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new token for the specified Image and Kind.
	 */
	public MyToken(int kind, String image)
	{
		this.kind = kind;
		this.image = image;
	}

	public int realKind = 0;
	public String realImage = "";

	/**
	 * Returns a new Token object.
	 */

	public static final Token newToken(int ofKind, String tokenImage)
	{
		return new MyToken(ofKind, tokenImage);
	}
}

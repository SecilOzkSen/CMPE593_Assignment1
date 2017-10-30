package com.SecilSen.objects;

public class Message {
	
	public EMessages message;
	public BaseTypeObject content;
	
	public Message(EMessages message, BaseTypeObject content)
	{
		this.message = message;
		this.content = content;
	}

	public EMessages getMessage() {
		return message;
	}

	public void setMessage(EMessages message) {
		this.message = message;
	}

	public BaseTypeObject getContent() {
		return content;
	}

	public void setContent(BaseTypeObject content) {
		this.content = content;
	}
	
}

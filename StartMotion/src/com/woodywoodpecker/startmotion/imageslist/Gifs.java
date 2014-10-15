package com.woodywoodpecker.startmotion.imageslist;

import java.util.UUID;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

@ServerType("Gifs")
public class Gifs extends DataItem {
	@ServerProperty("Name")
	private String name;

	@ServerProperty("Data")
	private UUID data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getData() {
		return data;
	}

	public void setData(UUID data) {
		this.data = data;
	}
}
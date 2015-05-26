package com.github.tongca.ui.binding.provider;

import android.content.Context;

public class BaseResourceProvider implements IResourceProvider {
	private final Context context;

	public BaseResourceProvider(Context context) {
		this.context = context.getApplicationContext();
	}

	@Override
	public int getIdentifierByName(String name) {
		return context.getResources().getIdentifier(name, "id",
				context.getPackageName());
	}

}

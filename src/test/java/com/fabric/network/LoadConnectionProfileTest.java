package com.fabric.network;

import org.hyperledger.fabric.sdk.NetworkConfig;
import org.junit.Test;

public class LoadConnectionProfileTest {

	@Test
	public void testLoadConnectionProfile() throws Exception {
		String org = "exporterorg";
		LoadConnectionProfile profile = LoadConnectionProfile.getInstance();
		System.out.println(profile.getCaInfo(org).getRegistrars().iterator().next().getName());
		System.out.println(profile.getCaInfo(org).getRegistrars().iterator().next().getEnrollSecret());
	}
	
	
}

package com.fabric.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig.OrgInfo;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.junit.Test;

import com.fabric.config.Config;
import com.fabric.participant.UserContext;
import com.fabric.utility.UtilIBM;

public class HFClientTest {

	@Test
	public void testGetCreatedChannelInfo() throws Exception {
		String orgName = "exporterorg";

		LoadConnectionProfile config = LoadConnectionProfile.getInstance();

		HFClient client = HFClient.createNewInstance();
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		client.setCryptoSuite(cryptoSuite);

		OrgInfo orgInfo = config.getOrgInfo(orgName);
		Channel tradeChannel = getCreatedChannel(orgInfo, "tradechannel");

		tradeChannel.getChannelConfigurationBytes();

	}

	@Test
	public void testChannelCreation() throws Exception {
		//		String org = "exporterorg";
		LoadConnectionProfile config = LoadConnectionProfile.getInstance();
		ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File("./network/channel-artifacts/channel.tx"));
		Collection<OrgInfo> orgInfos = config.getConfig().getOrganizationInfos();
		List<OrgInfo> orgs = new ArrayList<OrgInfo>(orgInfos);
		
		HFClient client = HFClient.createNewInstance();
		
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		client.setCryptoSuite(cryptoSuite);
		
		ArrayList<byte[]> channelConfigurationSignatures = new ArrayList<byte[]>();
		client.channel
		for (OrgInfo org: orgs) {
			channelConfigurationSignatures.add(enrollAndSignChannelConfiguration(client, channelConfiguration, org));
			System.out.println("Org " + org.getName() + " enroll admin and sign configuration complete");
		}
		
		for (byte[] signature: channelConfigurationSignatures) {
			System.out.println("Signature: " + new String(signature));
		}
		String name = "orderer.trade.com";
		Properties ordererProperties = config.getConfig().getOrdererProperties(name);

		UserContext ordererAdmin = new UserContext();
		File ordererAdminPkFolder = new File(Config.ORDERER_USR_ADMIN_PK);
		File[] ordererAdminPkFiles = ordererAdminPkFolder.listFiles();
		File ordererAdminCertFolder = new File(Config.ORDERER_USR_ADMIN_CERT);
		File[] ordererAdminCertFiles = ordererAdminCertFolder.listFiles();

		Enrollment enrollOrdererAdmin = UtilIBM.getEnrollment(Config.ORDERER_USR_ADMIN_PK, ordererAdminPkFiles[0].getName(), 
				Config.ORDERER_USR_ADMIN_CERT, ordererAdminCertFiles[0].getName());

		ordererAdmin.setEnrollment(enrollOrdererAdmin);
		ordererAdmin.setMspId("TradeOrdererMSP");
		ordererAdmin.setName("ordererAdmin");

		client.setUserContext(ordererAdmin);

//		client.setUserContext(getAdminUserContext("importerorg"));
//		Channel tradeChannel = client.loadChannelFromConfig("tradechannel", config.getConfig());
		
		Orderer tradeOrderer = client.newOrderer(name, "grpcs://orderer.trade.com:7050", ordererProperties);
		
		Channel tradeChannel = client.newChannel("tradechannel", tradeOrderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration, ordererAdmin), channelConfigurationSignatures.get(0), channelConfigurationSignatures.get(1), channelConfigurationSignatures.get(2), channelConfigurationSignatures.get(3));
//		Channel tc = client.getChannel("tradechannel");
//		client.newChannel("tradechannel", tradeOrderer, )
		System.out.println("Is channel initialized? " + tradeChannel.isInitialized());
//		if (!tradeChannel.isInitialized())
//			tradeChannel.initialize();
		
	}

	private byte[] enrollAndSignChannelConfiguration(HFClient client, ChannelConfiguration channelConfiguration, OrgInfo org) throws Exception {
		
		UserContext userContext = getEnrolledAdminUserContext(org.getName());
		
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		client.setCryptoSuite(cryptoSuite);

		client.setUserContext(userContext);
		return client.getChannelConfigurationSignature(channelConfiguration, userContext);
	}

	private Channel getCreatedChannel(OrgInfo org, String channel) throws Exception {
		LoadConnectionProfile config = LoadConnectionProfile.getInstance();
		HFCAClient hfcaClient = HFCAClient.createNewInstance(LoadConnectionProfile.getCaInfo(org.getName()));
		Enrollment enrollment = hfcaClient.enroll("admin", "adminpw");

		UserContext userContext = new UserContext();
		userContext.setEnrollment(enrollment);
		userContext.setName("admin");
		userContext.setMspId(org.getMspId());

		HFClient client = HFClient.createNewInstance();
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		client.setCryptoSuite(cryptoSuite);

		client.setUserContext(userContext);

		String ordererName = "orderer.trade.com";
		Properties ordererProperties = config.getConfig().getOrdererProperties(ordererName);

		Orderer tradeOrderer = client.newOrderer(ordererName, "grpcs://orderer.trade.com:7050", ordererProperties);
		return client.newChannel(channel);
	}
	
	private UserContext getEnrolledAdminUserContext(String org) throws Exception {
		LoadConnectionProfile config = LoadConnectionProfile.getInstance();
		HFCAClient hfcaClient = HFCAClient.createNewInstance(LoadConnectionProfile.getCaInfo(org));
		Enrollment enrollment = hfcaClient.enroll("admin", "adminpw");

		UserContext userContext = new UserContext();
		userContext.setEnrollment(enrollment);
		userContext.setName("admin");
		userContext.setMspId(config.getConfig().getOrganizationInfo(org).getMspId());

		return userContext;
	}


}

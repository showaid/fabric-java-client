package com.fabric.utility;

import java.lang.reflect.InvocationTargetException;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;

import com.fabric.participant.UserContext;

public class ClientUtils {
	public UserContext getOrdererAdmin(HFClient client) throws Exception {
		String orderer = "orderer.trade.com";
		
		User userContext = new UserContext();
		client.setUserContext(userContext);
		return null;
		
	}
	
	public static void enrollOrgAdminAndSignConfig(String org, HFClient client) throws InvalidArgumentException, CryptoException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		client.setUserContext(null);
		
		getSubmitter(client, true, org);
	}
	
	public static UserContext getSubmitter(HFClient client, boolean peerOrgAdmin, String org) throws CryptoException, InvalidArgumentException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		if (peerOrgAdmin) {
			return getAdmin(client, org);
		} else {
			return getMember("admin", "adminpw", client, org);
		}
	}
	
	public static UserContext getAdmin(HFClient client, String org) throws CryptoException, InvalidArgumentException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		UserContext admin = new UserContext();
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		client.setCryptoSuite(cryptoSuite);
		return null;
	}
	
	private static UserContext getMember(String string, String string2, HFClient client, String org) {
		// TODO Auto-generated method stub
		return null;
	}
}

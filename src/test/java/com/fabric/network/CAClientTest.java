package com.fabric.network;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.junit.Test;

import com.fabric.participant.UserContext;

public class CAClientTest {
	String org = "exporterorg";
	String userId = "hlfuser2";
	String userSecret;
	@Test
	public void testAdminEnrollment() throws Exception {
        LoadConnectionProfile networkConfig = LoadConnectionProfile.getInstance();
        HFCAClient hfcaClient = HFCAClient.createNewInstance(LoadConnectionProfile.getCaInfo(org));
        Enrollment enrollment = hfcaClient.enroll("admin", "adminpw");
        System.out.println(enrollment.getCert().getBytes().toString());
	}

	@Test
	public void testUserRegister() throws Exception {
		
		LoadConnectionProfile networkConfig = LoadConnectionProfile.getInstance();
        HFCAClient hfcaClient = HFCAClient.createNewInstance(LoadConnectionProfile.getCaInfo(org));
        Enrollment enrollment = hfcaClient.enroll("admin", "adminpw");

        UserContext adminUser = new UserContext();
        adminUser.setName("admin");
        adminUser.setEnrollment(enrollment);
        adminUser.setMspId(networkConfig.getOrgInfo(org).getMspId());
        
        RegistrationRequest request = new RegistrationRequest(userId, org);
        request.setAffiliation("org1.department1");
		userSecret = hfcaClient.register(request , adminUser);
		System.out.println("User Secret: " + userSecret);
	}
	
	@Test
	public void testUserEnrollment() throws Exception {
		userSecret = "WnFIqBezJEXk";
		LoadConnectionProfile networkConfig = LoadConnectionProfile.getInstance();
        HFCAClient hfcaClient = HFCAClient.createNewInstance(LoadConnectionProfile.getCaInfo(org));
        
		Enrollment userEnrollment = hfcaClient.enroll(userId, userSecret);
		System.out.println(userEnrollment.getCert());
	}
}

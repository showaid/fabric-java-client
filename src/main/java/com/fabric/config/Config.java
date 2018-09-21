package com.fabric.config;

import java.io.File;

public class Config {
	
	public static final String CHANNEL_CONFIG_PATH = "network" + File.separator + "channel-artifacts/channel.tx";
	
	public static final String ORDERER_USR_BASE_PATH = "network" + File.separator + "crypto-config" + File.separator + "ordererOrganizations" + File.separator
			+ "trade.com" + File.separator + "users" + File.separator + "Admin@trade.com"
			+ File.separator + "msp";
	
	public static final String ORDERER_USR_ADMIN_PK = ORDERER_USR_BASE_PATH + File.separator + "keystore";
	public static final String ORDERER_USR_ADMIN_CERT = ORDERER_USR_BASE_PATH + File.separator + "admincerts";
	
	public static final String CHAINCODE_ROOT_DIR = "chaincode";
	
	public static final String CHAINCODE_1_NAME = "fabcar";
	
	public static final String CHAINCODE_1_PATH = "github.com/fabcar";
	
	public static final String CHAINCODE_1_VERSION = "1";


}

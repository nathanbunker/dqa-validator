package org.immregistries.dqa.validator.address;

import org.immregistries.dqa.vxu.hl7.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This does nothing except provide a default implementation. 
 * @author Josh Hull
 *
 */
public enum AddressCleanserDefault implements AddressCleanser {
	INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressCleanserDefault.class);
	
	public void cleanThisAddress(Address a) {
		LOGGER.info("AddressCleansing not implemented. ");
	}
}
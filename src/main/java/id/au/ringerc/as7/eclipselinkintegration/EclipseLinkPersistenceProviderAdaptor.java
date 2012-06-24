package id.au.ringerc.as7.eclipselinkintegration;

import java.util.Map;

import org.jboss.as.jpa.spi.JtaManager;
import org.jboss.as.jpa.spi.ManagementAdaptor;
import org.jboss.as.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;

public class EclipseLinkPersistenceProviderAdaptor implements
		PersistenceProviderAdaptor {
	
	private final Logger logger = Logger.getLogger(EclipseLinkPersistenceProviderAdaptor.class);
	
	private static final String 
		ECLIPSELINK_TARGET_SERVER = "eclipselink.target-server",
		ECLIPSELINK_ARCHIVE_FACTORY = "eclipselink.archive.factory",
		ECLIPSELINK_LOGGING_LOGGER = "eclipselink.logging.logger";
	
	private final boolean hasJTABug;
	
	public EclipseLinkPersistenceProviderAdaptor() {
		logger.trace("EclipseLink integration activated");
		int major, minor, rev;
		try {
			String[] eclipseLinkVersion = org.eclipse.persistence.Version.getVersion().split("\\.");
			major = Integer.parseInt(eclipseLinkVersion[0]);
			minor = Integer.parseInt(eclipseLinkVersion[1]);
			rev = Integer.parseInt(eclipseLinkVersion[2]);
		} catch (NumberFormatException ex) {
			logger.warn("Could not parse EclipseLink version string " + ex + ", using default configuration");
			major = -1;
			minor = -1;
			rev = -1;
		}
		if (major < 2) {
			logger.warn("EclipseLink 1.x is not tested. You are running version " + major + '.' + minor + '.' + rev + ". ");
			hasJTABug = true;
		} else {
			// Version-specific handling here
			if ( (major == 2 && minor == 3 && rev <= 2) || (major == 2 && minor < 3) ) {
				logger.info("Enabling workaronud for EclipseLink bug 365704 for EclipseLink 2.3.2 and older");
				hasJTABug = true;
			} else {
				hasJTABug = false;
			}
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addProviderProperties(Map properties, PersistenceUnitMetadata pu) {
		if (!properties.containsKey(ECLIPSELINK_TARGET_SERVER)) {
			if (hasJTABug) {
				properties.put(ECLIPSELINK_TARGET_SERVER, JBossAS7ServerPlatform.class.getName());				
			} else {
				properties.put(ECLIPSELINK_TARGET_SERVER, "JBoss");
			}
			properties.put(ECLIPSELINK_ARCHIVE_FACTORY, JBossArchiveFactoryImpl.class.getName());
			properties.put(ECLIPSELINK_LOGGING_LOGGER, JBossLogger.class.getName());
		}
		logger.trace("Property " + ECLIPSELINK_TARGET_SERVER + " set to " + properties.get(ECLIPSELINK_TARGET_SERVER));
		logger.trace("Property " + ECLIPSELINK_ARCHIVE_FACTORY + " set to " + properties.get(ECLIPSELINK_ARCHIVE_FACTORY));
		logger.trace("Property " + ECLIPSELINK_LOGGING_LOGGER + " set to " + properties.get(ECLIPSELINK_LOGGING_LOGGER));
	}
	
	@Override
	public void injectJtaManager(JtaManager jtaManager) {
		// No action required, EclipseLink looks this up from JNDI
	}

	@Override
	public void addProviderDependencies(ServiceRegistry registry,
			ServiceTarget target, ServiceBuilder<?> builder,
			PersistenceUnitMetadata pu) {
		// No action required
	}

	@Override
	public void beforeCreateContainerEntityManagerFactory(
			PersistenceUnitMetadata pu) {
		// no action required
	}

	@Override
	public void afterCreateContainerEntityManagerFactory(
			PersistenceUnitMetadata pu) {
		// TODO: Force creation of metamodel here?
	}

	@Override
	public ManagementAdaptor getManagementAdaptor() {
		// no action required
		return null;
	}

	@Override
	public void cleanup(PersistenceUnitMetadata pu) {
		// no action required		
	}
	
}

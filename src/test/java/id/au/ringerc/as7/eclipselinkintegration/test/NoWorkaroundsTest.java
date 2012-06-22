package id.au.ringerc.as7.eclipselinkintegration.test;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This base test class uses a regular persistence.xml . It doesn't
 * explicitly list persistent classes, relying on
 * include-unlist-classes, and it doesn't set a custom server
 * adapter.
 * 
 * It should pass with a correctly configured persistence.xml
 * on EclipseLink 2.4.0 or 2.3.3 .
 * 
 * @author Craig Ringer <ringerc@ringerc.id.au>
 */
@RunWith(Arquillian.class)
public class NoWorkaroundsTest extends TestBase {

	@Deployment
	public static WebArchive makeDeployment() throws IOException {
		return TestBase.makeDeployment("persistence.xml");
	}

	@Test
	public void checkEclipseVersion() {
		super.checkEclipseVersion();
	}

	@Test
	public void ensureInjected() {
		super.ensureInjected();
	}

	@Test
	public void dynamicMetaModelWorks() {
		super.dynamicMetaModelWorks();
	}

	@Test
	public void isTransactional() {
		super.isTransactional();
	}

	@Test
	public void databaseAccessWorks() {
		super.databaseAccessWorks();
	}
	

}

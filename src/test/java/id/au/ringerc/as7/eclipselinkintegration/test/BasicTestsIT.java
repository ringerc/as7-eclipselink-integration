package id.au.ringerc.as7.eclipselinkintegration.test;

import java.io.File;
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
 * adapter, instead relying on the integration to take care of all that
 * for it.
 * 
 * This runs as an integration test after the library has been built,
 * deploying its own bundled copy of the integration library so it doesn't
 * have to be present on the server.
 * 
 * @author Craig Ringer <ringerc@ringerc.id.au>
 */
@RunWith(Arquillian.class)
public class BasicTestsIT extends TestBase {

	@Deployment
	public static WebArchive makeDeployment() throws IOException {
		WebArchive war = TestBase.makeDeployment("persistence.xml")
				// FIXME: Avoid hard coded library version by using ShrinkWrap Maven Dependency Resolver
				.addAsLibrary(new File("target/jboss-as-jpa-eclipselink-1.1.0.jar"))
				// FIXME: we should be able to deploy EclipseLink as a local library in WEB-INF/lib
				// and exclude org.eclipse.persistence via jboss-deployment-structure.xml, but 
				// it doesn't work right now. See https://community.jboss.org/thread/201605
				//
				//.addAsLibrary(new File("target/as7module/org/eclipse/persistence/main/eclipselink-2.4.0-RC2.jar"))
				//
				// Add a deployment structure descriptor that stops AS7 adding a system level EclipseLink
				// dependency and prevents any system-installed copy of this integration library
				// from becoming visible.
				//
				.addAsWebInfResource(new File("src/test/resources/META-INF/jboss-deployment-structure.xml"), "jboss-deployment-structure.xml");
		TestBase.printArchive(war);
		return war;
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

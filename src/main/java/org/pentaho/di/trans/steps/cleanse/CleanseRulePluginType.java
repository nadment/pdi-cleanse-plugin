/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.cleanse;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.plugins.BasePluginType;
import org.pentaho.di.core.plugins.JarFileAnnotationPlugin;
import org.pentaho.di.core.plugins.KettleURLClassLoader;
import org.pentaho.di.core.plugins.PluginAnnotationType;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.PluginFolderInterface;
import org.pentaho.di.core.plugins.PluginMainClassType;
import org.pentaho.di.core.plugins.PluginTypeInterface;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class represents the cleanse rule plugin type.
 * 
 * @author Nicolas ADMENT
 *
 */
@PluginMainClassType(CleanseProcessor.class)
@PluginAnnotationType(CleanseRule.class)
public class CleanseRulePluginType extends BasePluginType implements PluginTypeInterface {

	private static CleanseRulePluginType INSTANCE;

	public static CleanseRulePluginType getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CleanseRulePluginType();
		}
		return INSTANCE;
	}

	private CleanseRulePluginType() {
		super(CleanseRule.class, "CLEANSE_RULE", "Cleanse rule");
		populateFolders("plugins");
	}

	public void searchPlugins() throws KettlePluginException {
		// registerNatives();
		registerPluginJars();
		// registerXmlPlugins();
	}

	protected void registerXmlPlugins() throws KettlePluginException {
		for (PluginFolderInterface folder : pluginFolders) {

			if (folder.isPluginXmlFolder()) {
				List<FileObject> pluginXmlFiles = findPluginXmlFiles(folder.getFolder());
				for (FileObject file : pluginXmlFiles) {

					try {
						Document document = XMLHandler.loadXMLFile(file);
						Node pluginNode = XMLHandler.getSubNode(document, "plugin");
						if (pluginNode != null) {
							registerPluginFromXmlResource(pluginNode, KettleVFS.getFilename(file.getParent()),
									this.getClass(), false, file.getParent().getURL());
						}
					} catch (Exception e) {
						// We want to report this plugin.xml error, perhaps an
						// XML typo or something like that...
						//
						log.logError("Error found while reading plugin.xml file: " + file.getName().toString(), e);
					}
				}
			}
		}
	}

	protected void registerPluginJars() throws KettlePluginException {

		List<JarFileAnnotationPlugin> jarFilePlugins = findAnnotatedClassFiles(CleanseRule.class.getName());
		for (JarFileAnnotationPlugin jarFilePlugin : jarFilePlugins) {

			URLClassLoader urlClassLoader = createUrlClassLoader(jarFilePlugin.getJarFile(),
					getClass().getClassLoader());

			try {
				// TODO: BUG
				// Class<?> clazz = urlClassLoader.loadClass(
				// jarFilePlugin.getClassName() );
				Class<?> clazz = this.getClass().getClassLoader().loadClass(jarFilePlugin.getClassName());
				if (clazz == null) {
					throw new KettlePluginException("Unable to load class: " + jarFilePlugin.getClassName());
				}
				List<String> libraries = new ArrayList<String>();
				java.lang.annotation.Annotation annotation = null;
				try {

					// Bug annotation is null, if class is not loaded with the
					// same class loader
					annotation = clazz.getAnnotation(CleanseRule.class);

					String jarFilename = URLDecoder.decode(jarFilePlugin.getJarFile().getFile(), "UTF-8");
					libraries.add(jarFilename);
					FileObject fileObject = KettleVFS.getFileObject(jarFilename);
					FileObject parentFolder = fileObject.getParent();
					String parentFolderName = KettleVFS.getFilename(parentFolder);
					String libFolderName = null;
					if (parentFolderName.endsWith(Const.FILE_SEPARATOR + "lib")) {
						libFolderName = parentFolderName;
					} else {
						libFolderName = parentFolderName + Const.FILE_SEPARATOR + "lib";
					}

					PluginFolder folder = new PluginFolder(libFolderName, false, false, searchLibDir);
					FileObject[] jarFiles = folder.findJarFiles(true);

					if (jarFiles != null) {
						for (FileObject jarFile : jarFiles) {

							String fileName = KettleVFS.getFilename(jarFile);

							// If the plugin is in the lib folder itself, we'll
							// ignore it here
							if (fileObject.equals(jarFile)) {
								continue;
							}
							libraries.add(fileName);
						}
					}
				} catch (Exception e) {
					throw new KettlePluginException("Unexpected error loading class " + clazz.getName()
							+ " of plugin type: " + CleanseRule.class, e);
				}

				handlePluginAnnotation(clazz, annotation, libraries, false, jarFilePlugin.getPluginFolder());
			} catch (Exception e) {
				// Ignore for now, don't know if it's even possible.
				LogChannel.GENERAL
						.logError("Unexpected error registering jar plugin file: " + jarFilePlugin.getJarFile(), e);
			} finally {
				if (urlClassLoader != null && urlClassLoader instanceof KettleURLClassLoader) {
					((KettleURLClassLoader) urlClassLoader).closeClassLoader();
				}
			}
		}
	}

	/**
	 * Scan & register internal cleanse rule plugins
	 */
	@Override
	protected void registerNatives() throws KettlePluginException {
	}

	public void registerPlugin(Class<? extends CleanseProcessor> clazz) throws KettlePluginException {
		CleanseRule annotation = clazz.getAnnotation(CleanseRule.class);

		URL pluginFolder = null;
		List<String> libraries = new ArrayList<String>();

		handlePluginAnnotation(clazz, annotation, libraries, true, pluginFolder);
	}

	@Override
	protected String extractCategory(Annotation annotation) {
		return ((CleanseRule) annotation).category();
	}

	@Override
	protected String extractDesc(Annotation annotation) {
		return ((CleanseRule) annotation).description();
	}

	@Override
	protected String extractID(Annotation annotation) {
		return ((CleanseRule) annotation).id();
	}

	@Override
	protected String extractName(Annotation annotation) {
		return ((CleanseRule) annotation).name();
	}

	@Override
	protected String extractImageFile(Annotation annotation) {
		return null;
	}

	@Override
	protected boolean extractSeparateClassLoader(Annotation annotation) {
		return false;
	}

	@Override
	protected String extractClassLoaderGroup(Annotation annotation) {
		return ((CleanseRule) annotation).classLoaderGroup();
	}

	@Override
	protected String extractI18nPackageName(Annotation annotation) {
		return ((CleanseRule) annotation).i18nPackageName();
	}

	@Override
	protected void addExtraClasses(Map<Class<?>, String> classMap, Class<?> clazz, Annotation annotation) {
	}

	@Override
	protected String extractDocumentationUrl(Annotation annotation) {
		return null;
	}

	@Override
	protected String extractCasesUrl(Annotation annotation) {
		return null;
	}

	@Override
	protected String extractForumUrl(Annotation annotation) {
		return null;
	}

	// @Override
	// public void handlePluginAnnotation(Class<?> clazz, Annotation annotation,
	// List<String> libraries,
	// boolean nativePluginType, URL pluginFolder) throws KettlePluginException
	// {
	//
	// // FIXME: annotation is null with method getAnnotation()
	// // annotation = getAnnotation(clazz, CleanseRule.class );
	//
	// // Class<?> clazz2 = NoneRule.class;
	// // CleanseRule annotation_good =
	// // clazz2.getAnnotation(CleanseRule.class);
	// // Annotation a[]= clazz.getAnnotations();
	//
	// String idList = extractID(annotation);
	// if (Utils.isEmpty(idList)) {
	// throw new KettlePluginException("No ID specified for plugin with class: "
	// + clazz.getName());
	// }
	//
	// super.handlePluginAnnotation(clazz, annotation, libraries,
	// nativePluginType, pluginFolder);
	//
	// // // Only one ID for now
	// // String[] ids = idList.split( "," );
	// // super.handlePluginAnnotation( clazz, annotation, libraries,
	// // nativePluginType, pluginFolder );
	// // PluginInterface plugin =
	// // PluginRegistry.getInstance().findPluginWithId(
	// // ShimDependentJobEntryPluginType.class, ids[0] );
	// // URL[] urls = new URL[libraries.size()];
	// // for ( int i = 0; i < libraries.size(); i++ ) {
	// // File jarfile = new File( libraries.get( i ) );
	// // try {
	// // urls[i] = new URL( URLDecoder.decode(
	// // jarfile.toURI().toURL().toString(), "UTF-8" ) );
	// // } catch ( Exception e ) {
	// // throw new KettlePluginException( e );
	// // }// }
	// //
	// // try {
	// // Set<String> librarySet = new HashSet<String>(libraries);
	// // KettleURLClassLoader classloader = classLoaderMap.get( librarySet );
	// // if ( classloader == null ) {
	// // classloader = new KettleURLClassLoader( urls,
	// // HadoopConfigurationBootstrap.getHadoopConfigurationProvider()
	// // .getActiveConfiguration().getHadoopShim().getClass().getClassLoader()
	// // );
	// // classLoaderMap.put( librarySet, classloader );
	// // }
	// // PluginRegistry.getInstance().addClassLoader( classloader, plugin );
	// // } catch ( ConfigurationException e ) {
	// // throw new KettlePluginException( e );
	// // }
	// }

}

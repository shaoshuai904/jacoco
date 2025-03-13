/*******************************************************************************
 * Copyright (c) 2009, 2022 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.types.Resource;
import org.jacoco.report.InputStreamSourceFileLocator;

/**
 * Source locator based on Ant file resources.
 */
class AntFilesLocator extends InputStreamSourceFileLocator {

	private final Map<String, Resource> resources;

	/**
	 *
	 * @param encoding
	 *            编码格式
	 * @param tabWidth
	 *            tab宽度
	 */
	public AntFilesLocator(final String encoding, final int tabWidth) {
		super(encoding, tabWidth);
		this.resources = new HashMap<String, Resource>();
	}

	/**
	 * Adds the given file resource as a potential source file.
	 *
	 * @param file
	 *            file resource to add
	 */
	void add(final Resource file) {
		String pathKey = file.getName().replace(File.separatorChar, '/');
		resources.put(pathKey, file);
	}

	@Override
	protected InputStream getSourceStream(final String path)
			throws IOException {
		final Resource file = resources.get(path);
		if (file == null) {
			return null;
		} else {
			return file.getInputStream();
		}
	}

}

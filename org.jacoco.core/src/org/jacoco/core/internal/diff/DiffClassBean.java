/*******************************************************************************
 * Copyright (c) 2009, 2023 Mountainminds GmbH & Co. KG and Contributors
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
package org.jacoco.core.internal.diff;

import java.util.List;

/**
 * diff class json bean
 */
public class DiffClassBean {
	private String className;
	private List<DiffMethodBean> diffMethods;
	private String type;

	// ----------- get / set -----------

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<DiffMethodBean> getDiffMethods() {
		return diffMethods;
	}

	public void setDiffMethods(List<DiffMethodBean> diffMethods) {
		this.diffMethods = diffMethods;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

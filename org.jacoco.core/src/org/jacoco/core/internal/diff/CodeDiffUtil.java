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
import java.util.Map;

/**
 * 差异代码处理类
 */
public class CodeDiffUtil {

	private final static String OPERATE_ADD = "add";

	/**
	 * 检测类是否在差异代码中
	 */
	public static DiffClassBean checkClassIn(
			Map<String, DiffClassBean> classInfos, String className) {
		if (null == classInfos || classInfos.isEmpty()) {
			return null;
		}
		return classInfos.get(className);
	}

	/**
	 * 检测方法是否在差异代码中
	 */
	public static Boolean checkMethodIn(DiffClassBean classInfoDto,
			String methodName, String desc) {
		if (null == classInfoDto || null == methodName) {
			return Boolean.FALSE;
		}
		// 如果是新增类，不用匹配方法，直接运行
		if (OPERATE_ADD.equalsIgnoreCase(classInfoDto.getType())) {
			return Boolean.TRUE;
		}
		List<DiffMethodBean> methodList = classInfoDto.getDiffMethods();
		if (null == methodList || methodList.isEmpty()) {
			return Boolean.FALSE;
		}
		return methodList.stream().anyMatch(m -> {
			// 匹配了方法，参数也需要校验
			if (methodName.equals(m.getMethodName())) {
				if (m.methodDesc != null) {
					return m.methodDesc.equals(desc);
				} else {
					return desc == null;
				}
			} else {
				return Boolean.FALSE;
			}
		});
	}

}

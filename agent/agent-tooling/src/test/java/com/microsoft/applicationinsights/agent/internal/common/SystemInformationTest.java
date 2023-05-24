// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.applicationinsights.agent.internal.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SystemInformationTest {
  private static final Logger logger = LoggerFactory.getLogger(SystemInformationTest.class);

  @Test
  void testOs() {
    if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX) {
      assertThat(
              SystemUtils.IS_OS_WINDOWS
                  ? SystemInformation.isWindows()
                  : SystemInformation.isLinux())
          .isTrue();
    } else {
      logger.warn("can't validate OS");
    }
  }

  @Test
  void testProcessId() {
    Integer.parseInt(SystemInformation.getProcessId());
  }
}

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.applicationinsights.agent.internal.classicsdk;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IRETURN;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import javax.annotation.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestNameHandlerClassFileTransformer implements ClassFileTransformer {

  private static final Logger logger =
      LoggerFactory.getLogger(RequestNameHandlerClassFileTransformer.class);

  private final String unshadedClassName =
      UnshadedSdkPackageName.get() + "/web/spring/RequestNameHandlerInterceptorAdapter";

  @Override
  @Nullable
  public byte[] transform(
      @Nullable ClassLoader loader,
      @Nullable String className,
      @Nullable Class<?> classBeingRedefined,
      @Nullable ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    if (!unshadedClassName.equals(className)) {
      return null;
    }

    try {
      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor cv = new RequestNameHandlerClassVisitor(cw);
      ClassReader cr = new ClassReader(classfileBuffer);
      cr.accept(cv, 0);
      return cw.toByteArray();
    } catch (Throwable t) {
      logger.error(t.getMessage(), t);
      return null;
    }
  }

  private static class RequestNameHandlerClassVisitor extends ClassVisitor {

    private final ClassWriter cw;

    private RequestNameHandlerClassVisitor(ClassWriter cw) {
      super(ASM9, cw);
      this.cw = cw;
    }

    @Override
    @Nullable
    public MethodVisitor visitMethod(
        int access,
        String name,
        String descriptor,
        @Nullable String signature,
        @Nullable String[] exceptions) {
      MethodVisitor mv = cw.visitMethod(access, name, descriptor, signature, exceptions);
      if (name.equals("preHandle")
          && descriptor.equals(
              "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/Object;)Z")) {
        // no-op the preHandle() method
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
        return null;
      } else {
        return mv;
      }
    }
  }
}

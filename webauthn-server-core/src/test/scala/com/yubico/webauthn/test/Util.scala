// Copyright (c) 2018, Yubico AB
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.yubico.webauthn.test

import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.cert.X509Certificate

import com.yubico.internal.util.CertificateParser
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMParser

import scala.language.reflectiveCalls
import scala.util.Try


object Util {

  def importCertFromPem(certPem: InputStream): X509Certificate =
    CertificateParser.parseDer(
      new PEMParser(new BufferedReader(new InputStreamReader(certPem)))
        .readObject()
        .asInstanceOf[X509CertificateHolder]
        .getEncoded
    )

  type Stepish[A] = { def validate(): Unit; def next(): A }
  case class StepWithUtilities[A](a: Stepish[A]) {
    def validations: Try[Unit] = Try(a.validate())
    def tryNext: Try[A] = Try(a.next())
  }
  implicit def toStepWithUtilities[A](a: Stepish[A]): StepWithUtilities[A] = StepWithUtilities(a)

}

package org.zella.procaas.client.errors

class ProcaasException(msg: String, inner: Throwable = null)
  extends RuntimeException(msg, inner) {}

class ProcessException(code: Int, msg: String, inner: Throwable = null)
  extends ProcaasException(s"Invalid exit code: $code msg", inner) {}
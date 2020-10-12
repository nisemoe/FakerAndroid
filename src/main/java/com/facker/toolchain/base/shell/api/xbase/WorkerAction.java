package com.facker.toolchain.base.shell.api.xbase;


public abstract class WorkerAction {
     public abstract void action() throws Exception ;//方法体全部异常抛出，有必要细化异常
}

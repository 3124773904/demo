package com.robot.tool.lang;

/**
 * @author drj
 * @title: MyException 提示错误
 * @description:
 * @date 2021年07月05日 14:34
 */
public class HintException extends RuntimeException{
    public HintException(){}
    public HintException(String message){
        super(message);
    }
}

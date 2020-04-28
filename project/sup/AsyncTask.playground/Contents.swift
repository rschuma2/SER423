//: Playground - noun: a place where people can play

import UIKit

import Foundation
import XCPlayground
//XCPSetExecutionShouldContinueIndefinitely(true) // depricated
XCPlaygroundPage.currentPage.needsIndefiniteExecution = true
/**
 * AsyncTask by Nikita Kurtin
 * see: http://pastebin.com/0HSd3jAE
 *Defines a task which executed asynchronously in background thread.
 *Every AsyncTask instance has 3 life cycle events:
 * 1. beforeTask execution (Optional) - executed on UI Main thread
 * 2. bagkroundTask execution - executed in background thread
 * 3. afterTask execution (Optional) - executed on UI Main thread
 *
 *When caller instantiates AsyncTask he\she can decide what data type to pass in and out, using
 * predefined generic types <BGParam,BGResult> where
 * BGParam - passed in to 'backgroundTask' from calling 'execute' method
 * BGResult - passed out from 'backgroundTask' to 'afterTask' method
 *
 *Usage examples:
 *
 * //Example 1
 *   AsyncTask(backgroundTask: {(p:String)->() in
 *     print(p);
 *   }).execute("Hello async");
 *
 * //Example 2
 *   let task=AsyncTask(beforeTask: {
 *       print("pre execution");
 *   },backgroundTask: {(p:Int)->String in
 *      if p>=0{return "Positive";}
 *      else {return "Negative";}
 *   }, afterTask: {(p:String)in
 *      print("\(p)");
 *   });
 *   task.execute("Bubu");
 *
 */
public class AsyncTask <BGParam,BGResult>{
    private var pre:(()->())?;//Optional closure -> called before the backgroundTask
    private var bgTask:(param:BGParam)->BGResult;//background task
    private var post:((param:BGResult)->())?;//Optional closure -> called after the backgroundTask
    /**
    *@param beforeTask Optional closure -> which called just before the background task
    *@param backgroundTask closure -> the background task functionality with generic param & return
    *@param afterTask Optional -> which called just after the background task
    */
    public init(beforeTask: (()->())?=nil, backgroundTask: (param:BGParam)->BGResult, afterTask:((param:BGResult)->())?=nil){
        self.pre=beforeTask;
        self.bgTask=backgroundTask;
        self.post=afterTask;
    }
    /**
     *Execution method for current backgroundTask with given parameter value in background thread.
     *@param BGParam passed as a parameter to backgroundTask
     */
    public func execute(param:BGParam){
        pre?()//if beforeTask exists - invoke it before bgTask
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_BACKGROUND, 0), {
            let bgResult=self.bgTask(param: param);//execute backgroundTask in background thread
            if(self.post != nil){//if afterTask exists - invoke it in UI thread after bgTask
                dispatch_async(dispatch_get_main_queue(),{self.post!(param: bgResult)});
            }
        });
    }
}

var str = "Hello, playground"
let task2=AsyncTask(
    beforeTask: {
    print("pre execution");//print 'pre execution' before backgroundTask
    },
    backgroundTask:{(p:Int)->String in//set BGParam to Int & BGResult to String
        if p>0{//check if execution value is bigger than zero
            return "positive"//pass String "poitive" to afterTask
        }
        return "negative";//otherwise pass String "negative"
    },
    afterTask: {(p:String) in
        print(p);//print background task result
});
task2.execute(1);//execute with value 1

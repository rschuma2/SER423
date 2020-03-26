/*
 * Copyright 2017 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: Example classes conversion to/from json and use of optionals
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version March 2017
 */

import UIKit

/*
 * First an example to demonstrate optionals, their declaration and usage. This example
 * shows a simple class and corresponding function returns is an optionally nil instance
 * of the class, or an optionally nil userId for a user. It shows ways to unwrap (safely and not)
 */
class User {
    // userId is declared to be an optional String so it may be nil or a string value
    var userId:String?
}

// getUser returns an Optional User, which is either a valid User, or an optional nil value.
// when getUser returns a User, the user's id may be nil or have a string value.
func getUser (name: String) -> User?{
    var ret: User? = nil
    if name.isEqual("Jim"){
        let aUser = User()
        aUser.userId = "JBuffet"
        ret = aUser
    } else if name.isEqual("Tim") {
        let aUser = User()
        // Tim has a User, but he has no ID
        ret = aUser
    }
    // Sue (or any other name) is a nil User
    return ret
}

// In the if below, jim may be nil, and if it is nil, the print won't be executed. This is
// optional binding. Optionally bind jim to user named Jim. Replace string Jim with Sue and it
// still works, but prints nothing. Replace string Jim with Tim and its a runtime error for trying
// to unwrap an optional string that's nil in the print.
if let jim = getUser(name: "Jim") {
    print("Jim's userId is \(jim.userId!)") // the ! unwraps the userId String? to a String
}
// Change the above print by removing the ! This prints an optional value and you'll see its noted in
// the output -- for Jim. Now it will work for Tim, but shows the nil userId. Sue still skips the print.

// Below works correctly for either Jim or Sue, but not for Tim. For Tim, same as above:
// any.userId is nil.userId which will except, as above. With Sue, the else is used since Sue's user is nil.
if let any = getUser(name: "Jim") {
    print("The userId is \(any.userId!)")
} else {
    print("The userId is unknown")
}

// Below works for either Jim, or Tim, but not Sue.
let anyOne : User = getUser(name: "Jim")! // works because getUser("Jim") isn't nil. anyOne can't be nil
//let another : User? = getUser(name: "Sue")! // generates unexpectantly found nil when unwrapping
let another : User? = getUser(name: "Sue") // works because another can be nil and we don't try to unwrap the User
// The example above shows how Swift doesn't force correct usage, but it does encourage you

// Below only works for Jim. A runtime error occurs for Tim (can't unwrap a nil userId -- second !.)
// and a runtime error occurs for Sue (getUser returns a nil User and you can't unwrap it -- first !.)
let aStr : String = getUser(name: "Jim")!.userId!
print (aStr)

// Above would be much better written as below, which is called Optional chaining in Swift.
// There can be any number of ?'s in the chain indicating cascading checks for nil.
// ? must occur before a func call, member lookup, or subscript operation.
// Any number of ?'s in an if-let expression work, so long as all ?'s adhere to this rule.
// change Jim to Tim and then to Sue. All work, but Tim and Sue fail for different reasons
if let aStr : String = getUser(name: "Jim")?.userId {
    print(aStr)
} else {
    print("no user found")
}

// A more cumbersome expression of the above would be nested optional binding as below
// early releases of Swift did not include optional chaining and the below was recommended.
// Optional chaining is a good thing, but sometimes you want to break the chain apart.
// change Jim to Tim and then to Sue to see all prints executed.
if let aUser = getUser(name: "Jim") {
    if let aUserId = aUser.userId {
        print(aUserId)
    } else {
        print("got a user, but no userId found")
    }
} else {
    print("no user found")
}
// end of optional example. Now lets look at Json as supported in the Apple API.

/*
 * Here is a simple student class that initializes from Json and deserializes to a Json string.
 * It provides an initializer to create an instance from a json string, and
 * a method to covert an instance back into a json string. The underlying
 * NSJSONSerialization class that's used has been in the api for some time and is the same as
 * used with Objective-C. Accessed in Swift using JSONSerialization. You should use this class
 * as the basis for creating your solution to the lab exercises. Also note that the code assumes
 * for json string format is correct. As with most assignments and examples we'll cover, input
 * validation is ignored, and can be ignored should you be asked to submit code in this course.
 */
class Student {
    var name: String
    var studentid: Int
    var takes: Array<String>
    init (jsonStr: String){
        self.name = ""
        self.studentid=0
        self.takes = [String]()
        if let data: Data = jsonStr.data(using: String.Encoding.utf8){
            do{
                let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:Any]
                self.name = (dict!["name"] as? String)!
                self.studentid = (dict!["studentid"] as? Int)!
                self.takes = (dict!["takes"] as? Array<String>)!
            } catch {
                print("unable to convert to dictionary")
                
            }
        }
    }
    
    func toJsonString() -> String {
        var jsonStr = "";
        let dict:[String:Any] = ["name": name, "studentid": studentid, "takes":takes] as [String : Any]
        do {
            let jsonData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }
}

// lets work both the initializer from Json and the toJsonString methods.
let s:Student = Student(jsonStr: "{\"name\":\"Tim\",\"studentid\":50,\"takes\":[\"Ser423\",\"Cse494\"]}")
print("s as json \(s.toJsonString())")


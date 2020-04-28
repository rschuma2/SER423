/*
 * Copyright 2020 Tim Lindquist,
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
 * Purpose: Example Student class showing conversion to/from json
 * using Foundation's JSONSerialization class. JSONSerialization
 * converts foundation objects to/from JSON. It assumes the JSON
 * to be parsed or produced from a foundation object is either
 * an array or a dictionary of JSON values. JSONSerialization supports
 * conversion from foundation objects to JSON, and from JSON to
 * foundation objects. It differs from Java's org.json classes
 * in that no JSONObject or JSONArray classes are provided. Instead,
 * only a few functions for conversion. The result is either an array
 * or a dictionary (json object)
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February, 2020
 */

import UIKit

/*
 * First, an example to demonstrate optionals, their declaration and usage. This example
 * shows a simple User class and corresponding function returning an optionally nil instance
 * of the class. It shows two ways to unwrap
 */
class User {
    // userId is declared to be an optional String so it may be nil or a string value
    var userId:String?
}

// getUser returns an Optional User, which is either a valid User, or an optional nil value.
func getUser (name: String) -> User?{
    var ret: User? = nil
    // I know how to handle Jim, Tim, and Joe, but no other user names (returns nil user).
    if name.isEqual("Jim"){
        let aUser = User()
        aUser.userId = "JBuffet"
        ret = aUser
    } else if name.isEqual("Tim") {
        let aUser = User()
        aUser.userId = "TLind"
        ret = aUser
    } else if name.isEqual("Joe") {
        // Jim and Tim have userIds, but Joe has nil as userId.
        let aUser = User()
        ret = aUser
    }
    return ret
}

// In the if below, tim may be nil, and if it is nil, the print won't be executed. This is called
// optional binding. Optionally bind tim. Change the String parameter to getUser to "Sue" to see
// the else clause. When a function returns an optional, this construction is accepted practice
// to unwrap it to a non-optional.
if let tim:User = getUser(name: "Sue") {
    print("Tim's userId is \(tim.userId!)") // the ! unwraps the userId String? to a String
} else {
    print("nil user returned")
}
// As noted above, changing the argument to getUser above may return a nil user.
// If in the print below, remove the ! and change the name to Tim.
// The ! is the unwrap an optional operator. This optional value and you'll see its noted in
// the output.
if let sue:User = getUser(name: "Sue") {
    print("Sue's userId is \(sue.userId!)")
} else {
    print("Sue's userId is unknown")
}
let any : User = getUser(name: "Tim")! // works because getUser("Tim") isn't nil
//let another : User = getUser(name: "Sue")! // generates unexpectantly found nil when unwrapping
// The example above shows how Swift doesn't force correct usage, but it does encourage.

let aStr : String = getUser(name: "Tim")!.userId!
print (aStr)
// above would be much better written as below. When Tim is replaced with either Sue or Joe,
// a runtime error occurs. Below is much better. It is called Optional chaining in Swift.
// there can be any number of ?'s in the chain indicating cascading checks for nil in 
// accessing properties optional?.aProp, indexing an array or dictionary optional?["abc"],
// or calling a method optional?.myFunc(with: "abc"). Any number of such evaluations may
// exist, such as: optional?.aOptProp?["abc"]?.aFunc(with: "xyz"). Note, aOptProp is a dictionary
// of objects of a class defining aFunc. However, the usage a below with explicit unwraping the userId
// could still generate a runtime error. To see this, change Sue to Joe.
if let aStr : String = getUser(name: "Sue")?.userId! {
    print(aStr)
} else {
    print("no user found")
}
// a more cumbersome expression is necessary to propertly unwrap the string optional.
// It would be nested optional binding as below
// early releases of Swift did not include optional chaining and the below was recommended.
// Optional chaining is a good thing.
// Replace the string "Sue" below with the string "Joe" to see the result of both else statements.
if let aUser = getUser(name: "Sue") {
    if let aUserId = aUser.userId {
        print(aUserId)
    } else {
        print("found a user, but did not find a userid.")
    }
} else {
    print("no user found")
}
// end of optional example.

/*
 * Here a simple student class that was created to demonstrate Foundation support for Json. It provides
 * a simple class that includes an initializer to create an instance from a json string, and
 * a method to covert an instance back into a json string. The underlying Foundation
 * JSONSerialization class that's used has been in the api for some time and is bridged with that same as
 * used with Objective-C. You should use this class as the basis for creating your solution to
 * the first lab exercise. Also note that the code assumes for json string format is correct.
 * as with most assignments and examples we'll cover, input validation is ignored, and can be
 * ignored by the code you submit as well.
 */
public class Student {
    var name: String
    var studentid: Int
    var takes: Array<String>
    
    init (jsonStr: String){
        self.name = ""
        self.studentid=0
        self.takes = [String]()
        if let data:Data = jsonStr.data(using: String.Encoding.utf8) as Data?{
            do{
                let dict = try JSONSerialization.jsonObject(with: data, options:.mutableContainers) as? [String:Any]
                self.name = (dict!["name"] as? String)!
                self.studentid = (dict!["studentid"] as? Int)!
                self.takes = (dict!["takes"] as? Array<String>)!
            } catch {
                print("unable to convert Json to a dictionary")
                
            }
        }
    }
    
    func toJsonString() -> String {
        var jsonStr = "";
        let dict:[String : Any] = ["name": name, "studentid": studentid, "takes":takes] as [String : Any]
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print("unable to convert dictionary to a Json Object with error: \(error)")
        }
        return jsonStr
    }
}

let s:Student = Student(jsonStr: "{\"name\":\"Tim\",\"studentid\":50,\"takes\":[\"Ser423\",\"Cse494\"]}")
print("Student s as json \(s.toJsonString())")


// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

# This file contains default retry manager to be used with retry statement.

@typeParam
public type RetriableError error;
//todo use distinct when grammer allowes
//public type RetriableError distinct error;

public type RetryManager abstract object {
 public function shouldRetry(error? e) returns boolean;
};

@typeParam
public type DefaultRetryManager object {
    private int count;
    public function init(int count = 3) {
        self.count = count;
    }
    public function shouldRetry(error? e) returns boolean {
        if e is RetriableError && self.count >  0 {
          self.count -= 1;
          return true;
        } else {
           return false;
        }
    }
};

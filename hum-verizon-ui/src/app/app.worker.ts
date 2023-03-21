/// <reference lib="webworker" />

import { Calculation } from "./component/user/dashboard/calculation";

addEventListener('message', ({ data }) => {
  let response;
  response = Calculation.checkConnectionStability(data);
  postMessage(response);
});

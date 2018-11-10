// import {StompConfig} from "@stomp/ng2-stompjs";

export const SERVER_API_URL = localStorage.getItem("serverUrl");
export const uri: string = localStorage.getItem("socketDir") + 'websocket/configuracion';
export const NOTIFICATION_TOPIC: string = '/buslocator/notificacion';

// export const stompConfig: StompConfig = {
//     url: uri,
//     headers: {},
//     heartbeat_in: 0,
//     heartbeat_out: 20000,
//     reconnect_delay: 5000,
//     debug: false,
// };

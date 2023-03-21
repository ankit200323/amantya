export class TopicData {
    topicName: any;
    isSubscribe: any;
    filterName: any;
    sessionId : any;

    public constructor(topicName: any, filterName: any, isSubscribe: any, sessionId:any) {
        this.filterName = filterName;
        this.topicName = topicName;
        this.isSubscribe = isSubscribe;
        this.sessionId = sessionId;
    }
}
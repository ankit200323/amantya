import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { WebSocketShareService } from './web-socket-share.service';

describe('WebSocketShareService', () => {
  let service: WebSocketShareService;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule], 
    providers: [WebSocketShareService]
  }));

  it('should be created', () => {
    const service: WebSocketShareService = TestBed.get(WebSocketShareService);
    expect(service).toBeTruthy();
   });
});

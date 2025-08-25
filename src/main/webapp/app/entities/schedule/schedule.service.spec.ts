import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ScheduleService from './schedule.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Schedule } from '@/shared/model/schedule.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Schedule Service', () => {
    let service: ScheduleService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ScheduleService();
      currentDate = new Date();
      elemDefault = new Schedule(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, currentDate, currentDate, 'LOW', currentDate, 'PRIVATE');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Schedule', async () => {
        const returnedFromService = {
          id: 123,
          startTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { startTime: currentDate, endTime: currentDate, completedAt: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Schedule', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Schedule', async () => {
        const returnedFromService = {
          title: 'BBBBBB',
          description: 'BBBBBB',
          location: 'BBBBBB',
          allDay: true,
          startTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          priority: 'BBBBBB',
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          visibility: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startTime: currentDate, endTime: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Schedule', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Schedule', async () => {
        const patchObject = {
          location: 'BBBBBB',
          allDay: true,
          startTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          visibility: 'BBBBBB',
          ...new Schedule(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startTime: currentDate, endTime: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Schedule', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Schedule', async () => {
        const returnedFromService = {
          title: 'BBBBBB',
          description: 'BBBBBB',
          location: 'BBBBBB',
          allDay: true,
          startTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          priority: 'BBBBBB',
          completedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          visibility: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startTime: currentDate, endTime: currentDate, completedAt: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Schedule', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Schedule', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Schedule', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});

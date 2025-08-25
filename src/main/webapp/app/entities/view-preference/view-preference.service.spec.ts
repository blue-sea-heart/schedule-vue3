import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ViewPreferenceService from './view-preference.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { ViewPreference } from '@/shared/model/view-preference.model';

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
  describe('ViewPreference Service', () => {
    let service: ViewPreferenceService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ViewPreferenceService();
      currentDate = new Date();
      elemDefault = new ViewPreference(123, 'DAY', currentDate, currentDate, 'MONDAY', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          lastStart: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lastEnd: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a ViewPreference', async () => {
        const returnedFromService = {
          id: 123,
          lastStart: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lastEnd: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { lastStart: currentDate, lastEnd: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ViewPreference', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ViewPreference', async () => {
        const returnedFromService = {
          defaultView: 'BBBBBB',
          lastStart: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lastEnd: dayjs(currentDate).format(DATE_TIME_FORMAT),
          weekStart: 'BBBBBB',
          showWeekend: true,
          ...elemDefault,
        };

        const expected = { lastStart: currentDate, lastEnd: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ViewPreference', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ViewPreference', async () => {
        const patchObject = {
          defaultView: 'BBBBBB',
          lastStart: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lastEnd: dayjs(currentDate).format(DATE_TIME_FORMAT),
          showWeekend: true,
          ...new ViewPreference(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { lastStart: currentDate, lastEnd: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ViewPreference', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ViewPreference', async () => {
        const returnedFromService = {
          defaultView: 'BBBBBB',
          lastStart: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lastEnd: dayjs(currentDate).format(DATE_TIME_FORMAT),
          weekStart: 'BBBBBB',
          showWeekend: true,
          ...elemDefault,
        };
        const expected = { lastStart: currentDate, lastEnd: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ViewPreference', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ViewPreference', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ViewPreference', async () => {
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

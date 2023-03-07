import http from 'k6/http';

export const options = {
  vus: 100,
  duration: '10s',
};

export default function () {
  const payload = JSON.stringify({
    pan: '1234123412341234',
    amount: '5.00',
  });
  const headers = { 'Content-Type': 'application/json' };
  http.post('http://localhost:8080/async-just-controller/purchases', payload, { headers });
}
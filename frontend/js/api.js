/**
 * API Module - NGO Donation Tracking System
 * Base URL: http://localhost:8081
 */
const API = {
  BASE: 'http://localhost:8081',

  // ===== FETCH WRAPPER =====
  async _fetch(method, endpoint, body = null, headers = {}) {
    const config = {
      method,
      headers: { 'Content-Type': 'application/json', ...headers },
    };
    if (body !== null) config.body = JSON.stringify(body);
    try {
      const res = await fetch(API.BASE + endpoint, config);
      const text = await res.text();
      let data;
      try { data = text ? JSON.parse(text) : null; }
      catch { data = text; }
      if (!res.ok) {
        const msg = (data && (data.message || data.error)) || `HTTP ${res.status}`;
        throw new Error(msg);
      }
      return data;
    } catch (e) {
      if (e instanceof TypeError && e.message.includes('fetch')) {
        throw new Error('Cannot connect to server. Is the backend running?');
      }
      throw e;
    }
  },
  _get(ep) { return API._fetch('GET', ep); },
  _post(ep, body) { return API._fetch('POST', ep, body); },
  _put(ep, body) { return API._fetch('PUT', ep, body || {}); },
  _delete(ep) { return API._fetch('DELETE', ep); },

  // ===== USERS =====
  users: {
    getAll() { return API._get('/users'); },
    login(email, password, role) { return API._post('/login', { email, password, role }); },
    register(data) { return API._post('/register', data); },
    getByRole(role) { return API._get(`/users/role/${role}`); },
    getCount() { return API._get('/users/count'); },
    getById(id) { return API._get(`/users/${id}`); },
  },

  // ===== FUND REQUESTS =====
  fundRequests: {
    getAll() { return API._get('/fund-requests'); },
    getOpen() { return API._get('/fund-requests/open'); },
    getByNgo(ngoId) { return API._get(`/fund-requests/ngo/${ngoId}`); },
    create(data) { return API._post('/fund-requests', data); },
    updateStatus(id, status) { return API._put(`/fund-requests/${id}/status/${status}`); },
    getCount() { return API._get('/fund-requests/count'); },
    getCountByStatus(status) { return API._get(`/fund-requests/count/${status}`); },
    getById(id) { return API._get(`/fund-requests/${id}`); },
  },

  // ===== DONATIONS =====
  donations: {
    getAll() { return API._get('/donations'); },
    create(data) { return API._post('/donations', data); },
    getByDonor(donorId) { return API._get(`/donations/donor/${donorId}`); },
    getByRequest(requestId) { return API._get(`/donations/request/${requestId}`); },
    getTotal() { return API._get('/donations/total'); },
    getCount() { return API._get('/donations/count'); },
  },

  // ===== MATERIAL REQUESTS =====
  materialRequests: {
    getAll() { return API._get('/material-requests'); },
    getOpen() { return API._get('/material-requests/open'); },
    getByNgo(ngoId) { return API._get(`/material-requests/ngo/${ngoId}`); },
    create(data) { return API._post('/material-requests', data); },
    updateStatus(id, status) { return API._put(`/material-requests/${id}/status/${status}`); },
    getCount() { return API._get('/material-requests/count'); },
    getById(id) { return API._get(`/material-requests/${id}`); },
  },

  // ===== MATERIAL DONATIONS =====
  materialDonations: {
    getAll() { return API._get('/material-donations'); },
    create(data) { return API._post('/material-donations', data); },
    getByDonor(donorId) { return API._get(`/material-donations/donor/${donorId}`); },
    getByRequest(requestId) { return API._get(`/material-donations/request/${requestId}`); },
    getCount() { return API._get('/material-donations/count'); },
  },

  // ===== FUND EXPENDITURES =====
  fundExpenditures: {
    getAll() { return API._get('/fund-expenditures'); },
    create(data) { return API._post('/fund-expenditures', data); },
    getByNgo(ngoId) { return API._get(`/fund-expenditures/ngo/${ngoId}`); },
    getPending() { return API._get('/fund-expenditures/pending'); },
    approve(id) { return API._put(`/fund-expenditures/${id}/approve`); },
    reject(id) { return API._put(`/fund-expenditures/${id}/reject`); },
    getById(id) { return API._get(`/fund-expenditures/${id}`); },
  },

  // ===== INVENTORY =====
  inventory: {
    getAll() { return API._get('/inventory'); },
    getByNgo(ngoId) { return API._get(`/inventory/ngo/${ngoId}`); },
    getById(id) { return API._get(`/inventory/${id}`); },
  },

  // ===== BENEFICIARIES =====
  beneficiaries: {
    getAll() { return API._get('/beneficiaries'); },
    getPending() { return API._get('/beneficiaries/pending'); },
    verify(id) { return API._put(`/beneficiaries/${id}/verify`); },
    reject(id) { return API._put(`/beneficiaries/${id}/reject`); },
    getById(id) { return API._get(`/beneficiaries/${id}`); },
    getByUserId(userId) { return API._get(`/beneficiaries/user/${userId}`); },
  },

  // ===== BENEFICIARY REQUESTS =====
  beneficiaryRequests: {
    getAll() { return API._get('/beneficiary-requests'); },
    create(data) { return API._post('/beneficiary-requests', data); },
    getPending() { return API._get('/beneficiary-requests/pending'); },
    approve(id) { return API._put(`/beneficiary-requests/${id}/approve`); },
    reject(id) { return API._put(`/beneficiary-requests/${id}/reject`); },
    getByBeneficiary(beneficiaryId) { return API._get(`/beneficiary-requests/beneficiary/${beneficiaryId}`); },
    getById(id) { return API._get(`/beneficiary-requests/${id}`); },
  },

  // ===== BENEFICIARY ALLOCATIONS =====
  beneficiaryAllocations: {
    getAll() { return API._get('/beneficiary-allocations'); },
    confirm(id) { return API._put(`/beneficiary-allocations/${id}/confirm`); },
    getByBeneficiary(beneficiaryId) { return API._get(`/beneficiary-allocations/beneficiary/${beneficiaryId}`); },
    getById(id) { return API._get(`/beneficiary-allocations/${id}`); },
  },

  // ===== ADMIN STATS =====
  adminStats: {
    get() { return API._get('/admin/stats'); },
  },

  // ===== AUDIT LOGS =====
  auditLogs: {
    getAll() { return API._get('/audit-logs'); },
    getRecent(limit = 50) { return API._get(`/audit-logs?limit=${limit}`); },
  },
};

// Freeze to prevent modification
Object.freeze(API);

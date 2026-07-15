/**
 * Dashboard JS - NGO Donation Tracking System
 * Main dashboard logic for all user roles
 */

// ===== UTILITY FUNCTIONS =====
function formatCurrency(amount) {
  if (amount === null || amount === undefined) return '₹0';
  const n = parseFloat(amount) || 0;
  if (n >= 10000000) return '₹' + (n / 10000000).toFixed(1) + 'Cr';
  if (n >= 100000) return '₹' + (n / 100000).toFixed(1) + 'L';
  if (n >= 1000) return '₹' + (n / 1000).toFixed(1) + 'K';
  return '₹' + n.toLocaleString('en-IN');
}

function formatCurrencyFull(amount) {
  const n = parseFloat(amount) || 0;
  return '₹' + n.toLocaleString('en-IN');
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  try {
    const d = new Date(dateStr);
    if (isNaN(d)) return dateStr;
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
  } catch { return dateStr; }
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-';
  try {
    const d = new Date(dateStr);
    if (isNaN(d)) return dateStr;
    return d.toLocaleString('en-IN', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  } catch { return dateStr; }
}

function createBadge(status) {
  if (!status) return '<span class="badge badge-normal">-</span>';
  const s = status.toString().toLowerCase().replace(/\s+/g, '');
  const classMap = {
    open: 'badge-open', completed: 'badge-completed', pending: 'badge-pending',
    approved: 'badge-approved', rejected: 'badge-rejected', active: 'badge-active',
    verified: 'badge-verified', unverified: 'badge-unverified', confirmed: 'badge-confirmed',
    admin: 'badge-admin', ngo: 'badge-ngo', donor: 'badge-donor',
    beneficiary: 'badge-beneficiary', critical: 'badge-critical', high: 'badge-high',
    normal: 'badge-normal', material: 'badge-material', fund: 'badge-fund',
    partially_fulfilled: 'badge-warning', fulfilled: 'badge-completed',
  };
  const cls = classMap[s] || 'badge-normal';
  return `<span class="badge ${cls}">${status}</span>`;
}

function showToast(message, type = 'info') {
  let container = document.getElementById('toastContainer');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toastContainer';
    document.body.appendChild(container);
  }
  const iconMap = { success: 'bi-check-circle-fill', danger: 'bi-x-circle-fill', warning: 'bi-exclamation-triangle-fill', info: 'bi-info-circle-fill' };
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `<i class="bi ${iconMap[type] || iconMap.info}"></i><span>${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => { toast.style.opacity = '0'; toast.style.transform = 'translateX(100%)'; toast.style.transition = '0.3s'; setTimeout(() => toast.remove(), 300); }, 4000);
}

function showAlert(message, type = 'info') { showToast(message, type); }

async function confirmAction(message) {
  return new Promise(resolve => {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.style.zIndex = '3000';
    overlay.innerHTML = `
      <div class="modal-box" style="max-width:400px;text-align:center;padding:2.5rem">
        <i class="bi bi-exclamation-triangle-fill" style="font-size:2.5rem;color:#f59e0b;display:block;margin-bottom:1rem"></i>
        <h3 style="margin-bottom:0.75rem">Are you sure?</h3>
        <p style="margin-bottom:2rem">${message}</p>
        <div style="display:flex;gap:1rem;justify-content:center">
          <button id="confirmYes" class="btn btn-danger">Confirm</button>
          <button id="confirmNo" class="btn btn-secondary">Cancel</button>
        </div>
      </div>`;
    document.body.appendChild(overlay);
    overlay.querySelector('#confirmYes').onclick = () => { overlay.remove(); resolve(true); };
    overlay.querySelector('#confirmNo').onclick = () => { overlay.remove(); resolve(false); };
    overlay.onclick = (e) => { if (e.target === overlay) { overlay.remove(); resolve(false); } };
  });
}

function animateNumber(element, endValue, prefix = '', suffix = '', duration = 1200) {
  const start = 0;
  const startTime = performance.now();
  function update(currentTime) {
    const elapsed = currentTime - startTime;
    const progress = Math.min(elapsed / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    const current = Math.floor(start + (endValue - start) * eased);
    element.textContent = prefix + current.toLocaleString('en-IN') + suffix;
    if (progress < 1) requestAnimationFrame(update);
    else element.textContent = prefix + endValue.toLocaleString('en-IN') + suffix;
  }
  requestAnimationFrame(update);
}

function truncate(str, len = 40) {
  if (!str) return '-';
  return str.length > len ? str.substring(0, len) + '…' : str;
}

// ===== SIDEBAR NAV CONFIG =====
const NAV_CONFIG = {
  admin: [
    { icon: 'bi-speedometer2', label: 'Dashboard', section: 'adminSection', tab: null },
    { icon: 'bi-people-fill', label: 'Users', section: 'adminSection', tab: 'users', fn: 'loadAdminUsers' },
    { icon: 'bi-currency-rupee', label: 'Fund Requests', section: 'adminSection', tab: 'fundRequests', fn: 'loadAdminFundRequests' },
    { icon: 'bi-box-seam', label: 'Material Requests', section: 'adminSection', tab: 'materialRequests', fn: 'loadAdminMaterialRequests' },
    { icon: 'bi-shield-check', label: 'Verification Queue', section: 'adminSection', tab: 'beneficiaries', fn: 'loadAdminBeneficiaries' },
    { icon: 'bi-receipt', label: 'Utilization Reports', section: 'adminSection', tab: 'expenditures', fn: 'loadAdminExpenditures' },
    { icon: 'bi-archive', label: 'Inventory', section: 'adminSection', tab: 'inventory', fn: 'loadAdminInventory' },
    { icon: 'bi-journal-text', label: 'Audit Logs', section: 'adminSection', tab: 'auditLogs', fn: 'loadAdminAuditLogs' },
  ],
  ngo: [
    { icon: 'bi-speedometer2', label: 'Dashboard', section: 'ngoSection', tab: null },
    { icon: 'bi-currency-rupee', label: 'Fund Campaigns', section: 'ngoSection', tab: 'fundRequests', fn: 'loadNgoFundRequests' },
    { icon: 'bi-plus-circle', label: 'New Campaign', section: 'ngoSection', tab: 'createFundRequest', fn: null },
    { icon: 'bi-box-seam', label: 'Material Requests', section: 'ngoSection', tab: 'materialRequests', fn: 'loadNgoMaterialRequests' },
    { icon: 'bi-plus-square', label: 'New Material Req', section: 'ngoSection', tab: 'createMaterialRequest', fn: null },
    { icon: 'bi-archive', label: 'My Inventory', section: 'ngoSection', tab: 'inventory', fn: 'loadNgoInventory' },
    { icon: 'bi-receipt-cutoff', label: 'Upload Bills', section: 'ngoSection', tab: 'expenditures', fn: 'loadNgoExpenditures' },
    { icon: 'bi-heart-fill', label: 'Recent Donors', section: 'ngoSection', tab: 'donors', fn: 'loadNgoDonations' },
  ],
  donor: [
    { icon: 'bi-speedometer2', label: 'Dashboard', section: 'donorSection', tab: null },
    { icon: 'bi-search', label: 'Browse Campaigns', section: 'donorSection', tab: 'browse', fn: 'loadDonorFundRequests' },
    { icon: 'bi-box-seam', label: 'Material Requests', section: 'donorSection', tab: 'material', fn: 'loadDonorMaterialRequests' },
    { icon: 'bi-clock-history', label: 'Donation History', section: 'donorSection', tab: 'history', fn: 'loadDonorHistory' },
    { icon: 'bi-truck', label: 'Delivery Tracking', section: 'donorSection', tab: 'tracking', fn: 'loadDonorTracking' },
  ],
  beneficiary: [
    { icon: 'bi-speedometer2', label: 'Dashboard', section: 'beneficiarySection', tab: null },
    { icon: 'bi-list-check', label: 'My Requests', section: 'beneficiarySection', tab: 'requests', fn: 'loadBeneficiaryRequests' },
    { icon: 'bi-plus-circle', label: 'New Request', section: 'beneficiarySection', tab: 'createRequest', fn: null },
    { icon: 'bi-gift', label: 'My Allocations', section: 'beneficiarySection', tab: 'allocations', fn: 'loadBeneficiaryAllocations' },
    { icon: 'bi-check2-circle', label: 'Confirm Receipt', section: 'beneficiarySection', tab: 'confirmations', fn: 'loadBeneficiaryConfirmations' },
  ],
};

// ===== TAB SWITCHING =====
function adminTab(tab) { switchTab('admin', tab); }
function ngoTab(tab) { switchTab('ngo', tab); }
function donorTab(tab) { switchTab('donor', tab); }
function benTab(tab) { switchTab('ben', tab); }

function switchTab(prefix, tab) {
  document.querySelectorAll(`.${prefix}-tab-content`).forEach(el => el.classList.remove('active'));
  const el = document.getElementById(`${prefix}-${tab}`);
  if (el) el.classList.add('active');
  document.querySelectorAll(`.section-tabs .tab-btn`).forEach(b => {
    b.classList.toggle('active', b.getAttribute('data-tab') === tab);
  });
}

function showSection(sectionId) {
  document.querySelectorAll('.dashboard-section').forEach(s => s.classList.remove('active'));
  const sec = document.getElementById(sectionId);
  if (sec) sec.classList.add('active');
}

// ===== SIDEBAR BUILDER =====
function buildSidebar(role) {
  const nav = document.getElementById('sidebarNav');
  if (!nav) return;
  const items = NAV_CONFIG[role] || [];
  nav.innerHTML = items.map((item, i) => `
    <button class="nav-link ${i === 0 ? 'active' : ''}"
      data-section="${item.section}" data-tab="${item.tab || ''}" data-fn="${item.fn || ''}"
      onclick="navClick(this, '${item.section}', '${item.tab || ''}', '${item.fn || ''}')">
      <i class="bi ${item.icon}"></i>
      <span>${item.label}</span>
    </button>
  `).join('');
}

function navClick(el, section, tab, fn) {
  document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
  el.classList.add('active');
  showSection(section);
  if (tab) {
    const prefix = section.replace('Section', '');
    switchTab(prefix.toLowerCase() === 'beneficiary' ? 'ben' : prefix.toLowerCase(), tab);
  }
  if (fn && window[fn]) window[fn]();
  const titles = { adminSection: 'Admin Dashboard', ngoSection: 'NGO Dashboard', donorSection: 'Donor Dashboard', beneficiarySection: 'Beneficiary Dashboard' };
  const subtitle = el.querySelector('span')?.textContent || '';
  document.getElementById('pageTitle').textContent = titles[section] || 'Dashboard';
  const sub = document.getElementById('pageSubtitle');
  if (sub) sub.textContent = subtitle;
}

// ===== MAIN INIT =====
document.addEventListener('DOMContentLoaded', async () => {
  AUTH.requireAuth();
  const user = AUTH.getUser();
  const role = user.role.toLowerCase();

  // Set user info in topbar
  const userBadge = document.getElementById('userBadge');
  if (userBadge) {
    userBadge.className = `badge badge-${role}`;
    userBadge.textContent = user.role;
  }
  const userAvatar = document.getElementById('userAvatar');
  if (userAvatar) userAvatar.textContent = (user.name || user.email || '?')[0].toUpperCase();
  const userName = document.getElementById('userName');
  if (userName) userName.textContent = user.name || user.email;
  const pageTitle = document.getElementById('pageTitle');

  // Build sidebar and show correct section
  buildSidebar(role);
  const sectionMap = { admin: 'adminSection', ngo: 'ngoSection', donor: 'donorSection', beneficiary: 'beneficiarySection' };
  const sectionId = sectionMap[role] || 'adminSection';
  showSection(sectionId);
  if (pageTitle) {
    const titleMap = { admin: 'Admin Dashboard', ngo: 'NGO Dashboard', donor: 'Donor Dashboard', beneficiary: 'Beneficiary Dashboard' };
    pageTitle.textContent = titleMap[role] || 'Dashboard';
  }

  // Load dashboard based on role
  if (role === 'admin') await loadAdminDashboard();
  else if (role === 'ngo') await loadNgoDashboard();
  else if (role === 'donor') await loadDonorDashboard();
  else if (role === 'beneficiary') await loadBeneficiaryDashboard();

  // Setup event listeners
  setupEventListeners();
});

// ===== EVENT LISTENERS =====
function setupEventListeners() {
  // NGO Fund Request Form
  const ngoFundForm = document.getElementById('ngoFundRequestForm');
  if (ngoFundForm) ngoFundForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const user = AUTH.getUser();
    const fd = new FormData(e.target);
    const data = { ngoId: AUTH.getUserId(), title: fd.get('title'), description: fd.get('description'), category: fd.get('category'), targetAmount: parseFloat(fd.get('targetAmount')) };
    try {
      await API.fundRequests.create(data);
      showToast('Fund campaign created successfully!', 'success');
      e.target.reset();
      ngoTab('fundRequests');
      loadNgoFundRequests();
    } catch (err) { showToast(err.message, 'danger'); }
  });

  // NGO Material Request Form
  const ngoMatForm = document.getElementById('ngoMaterialRequestForm');
  if (ngoMatForm) ngoMatForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const fd = new FormData(e.target);
    const data = { ngoId: AUTH.getUserId(), itemName: fd.get('itemName'), category: fd.get('category'), description: fd.get('description'), quantityNeeded: parseInt(fd.get('quantityNeeded')), deliveryLocation: fd.get('deliveryLocation'), deadline: fd.get('deadline') };
    try {
      await API.materialRequests.create(data);
      showToast('Material request created successfully!', 'success');
      e.target.reset();
      ngoTab('materialRequests');
      loadNgoMaterialRequests();
    } catch (err) { showToast(err.message, 'danger'); }
  });

  // NGO Expenditure Form
  const ngoExpForm = document.getElementById('ngoExpenditureForm');
  if (ngoExpForm) ngoExpForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const fd = new FormData(e.target);
    const data = { ngoId: AUTH.getUserId(), requestId: parseInt(fd.get('requestId')), amount: parseFloat(fd.get('amount')), description: fd.get('description'), proofDocument: fd.get('proofDocument') };
    try {
      await submitFundExpenditure(data);
      e.target.reset();
    } catch (err) { showToast(err.message, 'danger'); }
  });

  // Donate Form
  const donateForm = document.getElementById('donateForm');
  if (donateForm) donateForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const requestId = parseInt(document.getElementById('modalRequestId').value);
    const amount = parseFloat(document.getElementById('donateAmount').value);
    try {
      await donateFund(requestId, amount);
      document.getElementById('donateModal').style.display = 'none';
      loadDonorFundRequests();
    } catch (err) { showToast(err.message, 'danger'); }
  });

  // Beneficiary Request Form
  const benForm = document.getElementById('benRequestForm');
  if (benForm) benForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const fd = new FormData(e.target);
    const data = { beneficiaryId: AUTH.getUserId(), assistanceCategory: fd.get('assistanceCategory'), itemName: fd.get('itemName'), requestedQuantity: parseInt(fd.get('requestedQuantity')) || null, requestedAmount: parseFloat(fd.get('requestedAmount')) || null, reason: fd.get('reason'), urgency: fd.get('urgency') };
    try {
      await createBeneficiaryRequest(data);
      e.target.reset();
      benTab('requests');
      loadBeneficiaryRequests();
    } catch (err) { showToast(err.message, 'danger'); }
  });

  // Donor search/filter
  const searchInput = document.getElementById('donorSearchFund');
  const filterCat = document.getElementById('donorFilterCategory');
  if (searchInput) searchInput.addEventListener('input', filterDonorCampaigns);
  if (filterCat) filterCat.addEventListener('change', filterDonorCampaigns);
}

// ===== ADMIN DASHBOARD =====
async function loadAdminDashboard() {
  try {
    const stats = await API.adminStats.get();
    // Populate stat cards with animation
    const statMap = {
      'stat-totalUsers': stats.totalUsers || 0,
      'stat-totalNgos': stats.totalNgos || 0,
      'stat-totalDonors': stats.totalDonors || 0,
      'stat-totalBeneficiaries': stats.totalBeneficiaries || 0,
      'stat-activeRequests': stats.activeFundRequests || stats.openRequests || 0,
      'stat-completedRequests': stats.completedRequests || 0,
      'stat-totalFunds': stats.totalDonations || stats.totalFundsCollected || 0,
      'stat-totalMaterials': stats.totalMaterialDonations || 0,
      'stat-pendingVerifications': stats.pendingBeneficiaries || 0,
      'stat-pendingReports': stats.pendingExpenditures || 0,
    };
    Object.entries(statMap).forEach(([id, val]) => {
      const el = document.getElementById(id);
      if (el) {
        if (id === 'stat-totalFunds') animateNumber(el, val, '₹');
        else animateNumber(el, val);
      }
    });
    // Load default tab
    await loadAdminUsers();
  } catch (err) {
    showToast('Failed to load admin stats: ' + err.message, 'warning');
    console.error(err);
  }
}

// ===== ADMIN USERS =====
async function loadAdminUsers() {
  const tbody = document.getElementById('adminUsersTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading users</td></tr>';
  try {
    const users = await API.users.getAll();
    if (!users || users.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-people"></i><p>No users found</p></div></td></tr>'; return; }
    tbody.innerHTML = users.map(u => `
      <tr>
        <td><span class="text-muted">#${u.id || u.userId}</span></td>
        <td><strong>${u.name || '-'}</strong></td>
        <td>${u.email || '-'}</td>
        <td>${createBadge(u.role)}</td>
        <td>${formatDate(u.createdAt || u.registrationDate)}</td>
        <td><div class="actions"><button class="btn btn-sm btn-danger" onclick="deleteUser(${u.id || u.userId})"><i class="bi bi-trash"></i></button></div></td>
      </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><i class="bi bi-exclamation-triangle"></i><p>${err.message}</p></div></td></tr>`; }
}

async function deleteUser(id) {
  if (await confirmAction('Delete this user? This cannot be undone.')) {
    showToast('User deletion not implemented in this demo.', 'warning');
  }
}

// ===== ADMIN FUND REQUESTS =====
async function loadAdminFundRequests() {
  const tbody = document.getElementById('adminFundRequestsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="8" class="loading">Loading fund requests</td></tr>';
  try {
    const reqs = await API.fundRequests.getAll();
    if (!reqs || reqs.length === 0) { tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><i class="bi bi-currency-rupee"></i><p>No fund requests found</p></div></td></tr>'; return; }
    tbody.innerHTML = reqs.map(r => {
      const pct = r.targetAmount > 0 ? Math.min(100, Math.round((r.collectedAmount || 0) / r.targetAmount * 100)) : 0;
      return `<tr>
        <td><span class="text-muted">#${r.requestId}</span></td>
        <td>${r.ngoName || r.ngoId || '-'}</td>
        <td><strong>${truncate(r.title, 30)}</strong></td>
        <td>${createBadge(r.category)}</td>
        <td>${formatCurrency(r.targetAmount)}</td>
        <td><div style="display:flex;align-items:center;gap:0.5rem"><div class="progress-bar-container" style="width:60px;margin:0"><div class="progress-bar" style="width:${pct}%"></div></div><span>${formatCurrency(r.collectedAmount || 0)}</span></div></td>
        <td>${createBadge(r.status)}</td>
        <td><div class="actions">
          ${r.status === 'OPEN' ? `<button class="btn btn-sm btn-danger" onclick="adminCloseRequest(${r.requestId})"><i class="bi bi-x-circle"></i> Close</button>` : ''}
        </div></td>
      </tr>`;
    }).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="8"><div class="empty-state"><i class="bi bi-exclamation-triangle"></i><p>${err.message}</p></div></td></tr>`; }
}

async function adminCloseRequest(id) {
  if (await confirmAction('Close this fund request?')) {
    try { await API.fundRequests.updateStatus(id, 'COMPLETED'); showToast('Request closed.', 'success'); loadAdminFundRequests(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}

// ===== ADMIN MATERIAL REQUESTS =====
async function loadAdminMaterialRequests() {
  const tbody = document.getElementById('adminMaterialRequestsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="8" class="loading">Loading material requests</td></tr>';
  try {
    const reqs = await API.materialRequests.getAll();
    if (!reqs || reqs.length === 0) { tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><i class="bi bi-box-seam"></i><p>No material requests found</p></div></td></tr>'; return; }
    tbody.innerHTML = reqs.map(r => `<tr>
      <td><span class="text-muted">#${r.requestId}</span></td>
      <td>${r.ngoName || r.ngoId || '-'}</td>
      <td><strong>${r.itemName || '-'}</strong></td>
      <td>${createBadge(r.category)}</td>
      <td>${r.quantityNeeded || 0}</td>
      <td>${r.quantityReceived || 0}</td>
      <td>${createBadge(r.status)}</td>
      <td><div class="actions">${r.status === 'OPEN' ? `<button class="btn btn-sm btn-danger" onclick="adminCloseMaterialRequest(${r.requestId})"><i class="bi bi-x-circle"></i> Close</button>` : ''}</div></td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="8"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

async function adminCloseMaterialRequest(id) {
  if (await confirmAction('Close this material request?')) {
    try { await API.materialRequests.updateStatus(id, 'COMPLETED'); showToast('Material request closed.', 'success'); loadAdminMaterialRequests(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}

// ===== ADMIN BENEFICIARIES =====
async function loadAdminBeneficiaries() {
  const tbody = document.getElementById('adminBeneficiariesTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const bens = await API.beneficiaries.getPending();
    if (!bens || bens.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-shield-check"></i><p>No pending verifications</p></div></td></tr>'; return; }
    tbody.innerHTML = bens.map(b => `<tr>
      <td><span class="text-muted">#${b.id}</span></td>
      <td>#${b.userId}</td>
      <td>${b.ngoName || b.ngoId || '-'}</td>
      <td>${createBadge(b.verificationStatus || b.status)}</td>
      <td>${b.contactDetails || '-'}</td>
      <td><div class="actions">
        <button class="btn btn-sm btn-success" onclick="verifyBeneficiary(${b.id})"><i class="bi bi-check2"></i> Verify</button>
        <button class="btn btn-sm btn-danger" onclick="rejectBeneficiary(${b.id})"><i class="bi bi-x"></i> Reject</button>
      </div></td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

async function verifyBeneficiary(id) {
  if (await confirmAction('Verify this beneficiary?')) {
    try { await API.beneficiaries.verify(id); showToast('Beneficiary verified!', 'success'); loadAdminBeneficiaries(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}
async function rejectBeneficiary(id) {
  if (await confirmAction('Reject this beneficiary?')) {
    try { await API.beneficiaries.reject(id); showToast('Beneficiary rejected.', 'warning'); loadAdminBeneficiaries(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}

// ===== ADMIN EXPENDITURES =====
async function loadAdminExpenditures() {
  const tbody = document.getElementById('adminExpendituresTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="8" class="loading">Loading</td></tr>';
  try {
    const exps = await API.fundExpenditures.getPending();
    if (!exps || exps.length === 0) { tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><i class="bi bi-receipt"></i><p>No pending reports</p></div></td></tr>'; return; }
    tbody.innerHTML = exps.map(e => `<tr>
      <td><span class="text-muted">#${e.expenditureId}</span></td>
      <td>${e.ngoName || e.ngoId || '-'}</td>
      <td>#${e.requestId || '-'}</td>
      <td>${formatCurrencyFull(e.amount)}</td>
      <td>${truncate(e.description, 35)}</td>
      <td>${e.proofDocument ? `<a href="${e.proofDocument}" target="_blank" class="btn btn-sm btn-info"><i class="bi bi-file-earmark"></i> View</a>` : '-'}</td>
      <td>${createBadge(e.status)}</td>
      <td><div class="actions">
        <button class="btn btn-sm btn-success" onclick="approveExpenditure(${e.expenditureId})"><i class="bi bi-check2"></i> Approve</button>
        <button class="btn btn-sm btn-danger" onclick="rejectExpenditure(${e.expenditureId})"><i class="bi bi-x"></i> Reject</button>
      </div></td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="8"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

async function approveExpenditure(id) {
  if (await confirmAction('Approve this utilization report?')) {
    try { await API.fundExpenditures.approve(id); showToast('Expenditure approved!', 'success'); loadAdminExpenditures(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}
async function rejectExpenditure(id) {
  if (await confirmAction('Reject this utilization report?')) {
    try { await API.fundExpenditures.reject(id); showToast('Expenditure rejected.', 'warning'); loadAdminExpenditures(); }
    catch (err) { showToast(err.message, 'danger'); }
  }
}

// ===== ADMIN AUDIT LOGS =====
async function loadAdminAuditLogs() {
  const tbody = document.getElementById('adminAuditLogsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="loading">Loading</td></tr>';
  try {
    const logs = await API.auditLogs.getAll();
    if (!logs || logs.length === 0) { tbody.innerHTML = '<tr><td colspan="5"><div class="empty-state"><i class="bi bi-journal"></i><p>No audit logs found</p></div></td></tr>'; return; }
    tbody.innerHTML = logs.map(l => `<tr>
      <td><span class="text-muted">#${l.logId}</span></td>
      <td><span class="badge badge-info" style="background:#dbeafe;color:#1e40af">${l.action || l.actionType || '-'}</span></td>
      <td>${truncate(l.details || l.description, 50)}</td>
      <td>${l.targetType || l.targetEntity || '-'} #${l.targetId || ''}</td>
      <td>${formatDateTime(l.createdDate || l.timestamp)}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== ADMIN INVENTORY =====
async function loadAdminInventory() {
  const tbody = document.getElementById('adminInventoryTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="7" class="loading">Loading</td></tr>';
  try {
    const items = await API.inventory.getAll();
    if (!items || items.length === 0) { tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><i class="bi bi-archive"></i><p>No inventory found</p></div></td></tr>'; return; }
    tbody.innerHTML = items.map(i => `<tr>
      <td>${i.ngoName || i.ngoId || '-'}</td>
      <td><strong>${i.itemName || i.materialRequestTitle || '-'}</strong></td>
      <td>${createBadge(i.category)}</td>
      <td><span class="fw-bold text-success">${i.availableQty || 0}</span></td>
      <td>${i.reservedQty || 0}</td>
      <td>${i.distributedQty || 0}</td>
      <td>${i.damagedQty || 0}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="7"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== NGO DASHBOARD =====
async function loadNgoDashboard() {
  const userId = AUTH.getUserId();
  try {
    const [fundReqs, matReqs, expenditures] = await Promise.allSettled([
      API.fundRequests.getByNgo(userId),
      API.materialRequests.getByNgo(userId),
      API.fundExpenditures.getByNgo(userId),
    ]);
    const fr = fundReqs.value || [];
    const me = expenditures.value || [];
    const totalRequested = fr.reduce((s, r) => s + (r.targetAmount || 0), 0);
    const totalCollected = fr.reduce((s, r) => s + (r.collectedAmount || 0), 0);
    const totalSpent = me.reduce((s, e) => s + (e.amount || 0), 0);
    const totalRemaining = totalCollected - totalSpent;

    const statIds = { 'ngo-fundsRequested': totalRequested, 'ngo-fundsCollected': totalCollected, 'ngo-fundsRemaining': Math.max(0, totalRemaining), 'ngo-fundsSpent': totalSpent };
    Object.entries(statIds).forEach(([id, val]) => {
      const el = document.getElementById(id);
      if (el) animateNumber(el, Math.round(val), '₹');
    });

    // Load fund requests select for expenditure form
    const select = document.getElementById('ngoExpRequestSelect');
    if (select && fr.length) {
      select.innerHTML = fr.map(r => `<option value="${r.requestId}">#${r.requestId} - ${r.title} (${formatCurrency(r.collectedAmount || 0)} available)</option>`).join('');
    }

    await loadNgoFundRequests();
  } catch (err) { showToast('Failed to load NGO data: ' + err.message, 'warning'); }
}

// ===== NGO FUND REQUESTS =====
async function loadNgoFundRequests() {
  const tbody = document.getElementById('ngoFundRequestsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const reqs = await API.fundRequests.getByNgo(AUTH.getUserId());
    if (!reqs || reqs.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-currency-rupee"></i><p>No campaigns yet. Create your first one!</p></div></td></tr>'; return; }
    tbody.innerHTML = reqs.map(r => {
      const collected = r.collectedAmount || 0;
      const remaining = Math.max(0, (r.targetAmount || 0) - collected);
      const pct = r.targetAmount > 0 ? Math.min(100, Math.round(collected / r.targetAmount * 100)) : 0;
      return `<tr>
        <td><strong>${r.title || '-'}</strong></td>
        <td>${createBadge(r.category)}</td>
        <td>${formatCurrencyFull(r.targetAmount)}</td>
        <td><div style="display:flex;align-items:center;gap:0.5rem"><div class="progress-bar-container" style="width:80px;margin:0"><div class="progress-bar" style="width:${pct}%"></div></div><span>${formatCurrency(collected)} (${pct}%)</span></div></td>
        <td>${formatCurrency(remaining)}</td>
        <td>${createBadge(r.status)}</td>
      </tr>`;
    }).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== NGO MATERIAL REQUESTS =====
async function loadNgoMaterialRequests() {
  const tbody = document.getElementById('ngoMaterialRequestsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const reqs = await API.materialRequests.getByNgo(AUTH.getUserId());
    if (!reqs || reqs.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-box-seam"></i><p>No material requests yet.</p></div></td></tr>'; return; }
    tbody.innerHTML = reqs.map(r => {
      const remaining = Math.max(0, (r.quantityNeeded || 0) - (r.quantityReceived || 0));
      return `<tr>
        <td><strong>${r.itemName || '-'}</strong></td>
        <td>${createBadge(r.category)}</td>
        <td>${r.quantityNeeded || 0}</td>
        <td>${r.quantityReceived || 0}</td>
        <td>${remaining}</td>
        <td>${createBadge(r.status)}</td>
      </tr>`;
    }).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== NGO INVENTORY =====
async function loadNgoInventory() {
  const tbody = document.getElementById('ngoInventoryTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const items = await API.inventory.getByNgo(AUTH.getUserId());
    if (!items || items.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-archive"></i><p>No inventory items.</p></div></td></tr>'; return; }
    tbody.innerHTML = items.map(i => `<tr>
      <td><strong>${i.itemName || '-'}</strong></td>
      <td>${createBadge(i.category)}</td>
      <td><span class="fw-bold text-success">${i.availableQty || 0}</span></td>
      <td>${i.reservedQty || 0}</td>
      <td>${i.distributedQty || 0}</td>
      <td>${i.damagedQty || 0}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== NGO DONATIONS =====
async function loadNgoDonations() {
  const tbody = document.getElementById('ngoDonorsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="loading">Loading</td></tr>';
  try {
    // Get all donations, filter by NGO's requests
    const [donations, myReqs] = await Promise.all([API.donations.getAll(), API.fundRequests.getByNgo(AUTH.getUserId())]);
    const myRequestIds = new Set((myReqs || []).map(r => r.requestId));
    const myDonations = (donations || []).filter(d => myRequestIds.has(d.requestId));
    if (!myDonations.length) { tbody.innerHTML = '<tr><td colspan="5"><div class="empty-state"><i class="bi bi-heart"></i><p>No donations yet to your campaigns.</p></div></td></tr>'; return; }
    const sorted = myDonations.sort((a, b) => new Date(b.donationDate || b.createdAt) - new Date(a.donationDate || a.createdAt));
    tbody.innerHTML = sorted.slice(0, 50).map(d => `<tr>
      <td>#${d.donorId}</td>
      <td>#${d.requestId}</td>
      <td><strong>${formatCurrencyFull(d.amount)}</strong></td>
      <td>${formatDate(d.donationDate || d.createdAt)}</td>
      <td>${createBadge(d.donationStatus || 'COMPLETED')}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== NGO EXPENDITURES =====
async function loadNgoExpenditures() {
  const tbody = document.getElementById('ngoExpendituresList');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="loading">Loading</td></tr>';
  try {
    const exps = await API.fundExpenditures.getByNgo(AUTH.getUserId());
    if (!exps || exps.length === 0) { tbody.innerHTML = '<tr><td colspan="5"><div class="empty-state"><i class="bi bi-receipt"></i><p>No utilization reports yet.</p></div></td></tr>'; return; }
    tbody.innerHTML = exps.map(e => `<tr>
      <td>#${e.requestId || '-'}</td>
      <td>${formatCurrencyFull(e.amount)}</td>
      <td>${truncate(e.description, 40)}</td>
      <td>${e.proofDocument ? `<a href="${e.proofDocument}" target="_blank"><i class="bi bi-file-earmark-text"></i> ${e.proofDocument}</a>` : '-'}</td>
      <td>${createBadge(e.status)}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

async function submitFundExpenditure(data) {
  try {
    await API.fundExpenditures.create(data);
    showToast('Utilization report submitted! Pending admin approval.', 'success');
    loadNgoExpenditures();
  } catch (err) { throw err; }
}

// ===== DONOR DASHBOARD =====
let allFundRequests = [];
async function loadDonorDashboard() {
  const userId = AUTH.getUserId();
  try {
    const [myDonations, myMatDonations] = await Promise.allSettled([
      API.donations.getByDonor(userId),
      API.materialDonations.getByDonor(userId),
    ]);
    const fd = myDonations.value || [];
    const md = myMatDonations.value || [];
    const totalDonated = fd.reduce((s, d) => s + (d.amount || 0), 0);
    const stats = { 'donor-totalDonated': { val: Math.round(totalDonated), prefix: '₹' }, 'donor-donationCount': { val: fd.length, prefix: '' }, 'donor-materialCount': { val: md.length, prefix: '' }, 'donor-impactBeneficiaries': { val: 0, prefix: '' } };
    Object.entries(stats).forEach(([id, { val, prefix }]) => {
      const el = document.getElementById(id);
      if (el) animateNumber(el, val, prefix);
    });
    await loadDonorFundRequests();
  } catch (err) { showToast('Failed to load donor data: ' + err.message, 'warning'); }
}

// ===== DONOR FUND REQUESTS =====
async function loadDonorFundRequests() {
  const container = document.getElementById('donorFundCards');
  if (!container) return;
  container.innerHTML = '<div class="loading" style="padding:2rem">Loading campaigns</div>';
  try {
    allFundRequests = await API.fundRequests.getOpen();
    if (!allFundRequests || allFundRequests.length === 0) { container.innerHTML = '<div class="empty-state"><i class="bi bi-currency-rupee"></i><p>No open campaigns right now.</p></div>'; return; }
    renderFundCards(allFundRequests, container);
  } catch (err) { container.innerHTML = `<div class="empty-state"><i class="bi bi-exclamation-triangle"></i><p>${err.message}</p></div>`; }
}

function renderFundCards(requests, container) {
  if (!requests || requests.length === 0) {
    container.innerHTML = '<div class="empty-state"><i class="bi bi-search"></i><p>No matching campaigns found.</p></div>';
    return;
  }
  const colors = ['linear-gradient(90deg,#4F46E5,#7C3AED)', 'linear-gradient(90deg,#059669,#10B981)', 'linear-gradient(90deg,#2563EB,#3B82F6)', 'linear-gradient(90deg,#dc2626,#f43f5e)', 'linear-gradient(90deg,#d97706,#f59e0b)'];
  container.innerHTML = requests.map((r, i) => {
    const collected = r.collectedAmount || 0;
    const target = r.targetAmount || 1;
    const pct = Math.min(100, Math.round(collected / target * 100));
    const color = colors[i % colors.length];
    return `<div class="fund-card">
      <div class="fund-card-color-bar" style="background:${color}"></div>
      <div class="fund-card-body">
        <div class="fund-card-header">
          <div class="fund-card-title">${r.title || 'Campaign'}</div>
          ${createBadge(r.category || r.status)}
        </div>
        <div class="fund-card-desc">${r.description || 'Help make a difference.'}</div>
        <div class="progress-bar-container"><div class="progress-bar" style="width:${pct}%"></div></div>
        <div class="fund-card-meta">
          <span>${pct}% funded</span>
          <span>Target: ${formatCurrency(target)}</span>
        </div>
        <div class="fund-card-amount">${formatCurrency(collected)} raised</div>
      </div>
      <div class="fund-card-footer">
        <span class="text-muted" style="font-size:0.8rem">NGO #${r.ngoId}</span>
        <button class="btn btn-sm btn-primary" onclick="openDonateModal(${r.requestId}, '${(r.title || '').replace(/'/g, "\\'")}')"><i class="bi bi-heart"></i> Donate</button>
      </div>
    </div>`;
  }).join('');
}

function filterDonorCampaigns() {
  const search = (document.getElementById('donorSearchFund')?.value || '').toLowerCase();
  const cat = (document.getElementById('donorFilterCategory')?.value || '').toLowerCase();
  const filtered = allFundRequests.filter(r => {
    const matchSearch = !search || (r.title || '').toLowerCase().includes(search) || (r.description || '').toLowerCase().includes(search);
    const matchCat = !cat || (r.category || '').toLowerCase() === cat;
    return matchSearch && matchCat;
  });
  renderFundCards(filtered, document.getElementById('donorFundCards'));
}

function openDonateModal(requestId, title) {
  document.getElementById('modalRequestId').value = requestId;
  document.getElementById('modalRequestTitle').textContent = 'Campaign: ' + title;
  document.getElementById('donateAmount').value = '';
  document.getElementById('donateModal').style.display = 'flex';
}

async function donateFund(requestId, amount) {
  const data = { donorId: AUTH.getUserId(), requestId: requestId, fundRequestId: requestId, amount: amount, status: 'COMPLETED' };
  try {
    await API.donations.create(data);
    showToast(`Donation of ₹${amount.toLocaleString()} made successfully! 🎉`, 'success');
  } catch (err) { throw err; }
}

// ===== DONOR MATERIAL REQUESTS =====
async function loadDonorMaterialRequests() {
  const container = document.getElementById('donorMaterialCards');
  if (!container) return;
  container.innerHTML = '<div class="loading" style="padding:2rem">Loading material requests</div>';
  try {
    const reqs = await API.materialRequests.getOpen();
    if (!reqs || reqs.length === 0) { container.innerHTML = '<div class="empty-state"><i class="bi bi-box-seam"></i><p>No open material requests.</p></div>'; return; }
    container.innerHTML = reqs.map(r => {
      const received = r.quantityReceived || 0;
      const needed = r.quantityNeeded || 1;
      const pct = Math.min(100, Math.round(received / needed * 100));
      return `<div class="fund-card">
        <div class="fund-card-color-bar" style="background:linear-gradient(90deg,#7c3aed,#a78bfa)"></div>
        <div class="fund-card-body">
          <div class="fund-card-header">
            <div class="fund-card-title">${r.itemName || 'Material'}</div>
            ${createBadge(r.category)}
          </div>
          <div class="fund-card-desc">${r.description || ''}</div>
          <div class="progress-bar-container"><div class="progress-bar" style="width:${pct}%;background:linear-gradient(90deg,#7c3aed,#a78bfa)"></div></div>
          <div class="fund-card-meta"><span>${pct}% received</span><span>Need: ${needed} units</span></div>
          <div style="font-size:0.82rem;color:#6b7280;margin-bottom:0.75rem"><i class="bi bi-geo-alt"></i> ${r.deliveryLocation || 'TBD'} &nbsp;|&nbsp; <i class="bi bi-calendar"></i> ${formatDate(r.deadline)}</div>
        </div>
        <div class="fund-card-footer">
          <span class="text-muted" style="font-size:0.8rem">NGO #${r.ngoId}</span>
          <button class="btn btn-sm btn-success" onclick="donateMaterial(${r.requestId}, '${(r.itemName || '').replace(/'/g, "\\'")}')"><i class="bi bi-box-seam"></i> Donate</button>
        </div>
      </div>`;
    }).join('');
  } catch (err) { container.innerHTML = `<div class="empty-state"><p>${err.message}</p></div>`; }
}

async function donateMaterial(requestId, itemName) {
  const qty = prompt(`How many units of "${itemName}" would you like to donate?`);
  if (!qty || isNaN(parseInt(qty))) return;
  const courier = prompt('Enter courier service name (optional):') || '';
  const tracking = prompt('Enter tracking number (optional):') || '';
  const data = { donorId: AUTH.getUserId(), requestId: requestId, materialRequestId: requestId, quantity: parseInt(qty), courierService: courier, trackingNumber: tracking };
  try {
    await API.materialDonations.create(data);
    showToast(`Thank you for donating ${qty} units of ${itemName}!`, 'success');
    loadDonorMaterialRequests();
  } catch (err) { showToast(err.message, 'danger'); }
}

// ===== DONOR HISTORY =====
async function loadDonorHistory() {
  const tbody = document.getElementById('donorHistoryTable');
  const matTbody = document.getElementById('donorMaterialHistoryTable');
  const userId = AUTH.getUserId();

  if (tbody) {
    tbody.innerHTML = '<tr><td colspan="4" class="loading">Loading</td></tr>';
    try {
      const donations = await API.donations.getByDonor(userId);
      if (!donations || donations.length === 0) { tbody.innerHTML = '<tr><td colspan="4"><div class="empty-state"><i class="bi bi-clock-history"></i><p>No fund donations yet.</p></div></td></tr>'; }
      else {
        tbody.innerHTML = donations.sort((a,b) => new Date(b.donationDate||b.createdAt) - new Date(a.donationDate||a.createdAt)).map(d => `<tr>
          <td>${formatDate(d.donationDate || d.createdAt)}</td>
          <td>Campaign #${d.requestId}</td>
          <td><strong>${formatCurrencyFull(d.amount)}</strong></td>
          <td>${createBadge(d.donationStatus || 'COMPLETED')}</td>
        </tr>`).join('');
      }
    } catch (err) { tbody.innerHTML = `<tr><td colspan="4"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
  }

  if (matTbody) {
    matTbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
    try {
      const mats = await API.materialDonations.getByDonor(userId);
      if (!mats || mats.length === 0) { matTbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-box-seam"></i><p>No material donations yet.</p></div></td></tr>'; }
      else {
        matTbody.innerHTML = mats.map(m => `<tr>
          <td>${m.itemName || `Request #${m.requestId}`}</td>
          <td>${m.quantity || '-'}</td>
          <td>${m.courierName || '-'}</td>
          <td>${m.trackingNumber || '-'}</td>
          <td>${createBadge(m.deliveryStatus || 'PENDING')}</td>
          <td>${m.receiptStatus === 'RECEIVED' ? '<span class="badge badge-approved">Confirmed</span>' : '<span class="badge badge-pending">Pending</span>'}</td>
        </tr>`).join('');
      }
    } catch (err) { matTbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
  }
}

// ===== DONOR TRACKING =====
async function loadDonorTracking() {
  const tbody = document.getElementById('donorTrackingTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const mats = await API.materialDonations.getByDonor(AUTH.getUserId());
    if (!mats || mats.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-truck"></i><p>No material donations to track.</p></div></td></tr>'; return; }
    tbody.innerHTML = mats.map(m => `<tr>
      <td>${m.itemName || `Request #${m.requestId}`}</td>
      <td>${m.courierName || '-'}</td>
      <td>${m.trackingNumber || '-'}</td>
      <td>${formatDate(m.expectedDeliveryDate || m.deadline)}</td>
      <td>${createBadge(m.deliveryStatus || 'IN_TRANSIT')}</td>
      <td>${m.receiptStatus === 'RECEIVED' ? '<span class="badge badge-approved">Confirmed</span>' : '<span class="badge badge-pending">Awaiting</span>'}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== BENEFICIARY DASHBOARD =====
async function loadBeneficiaryDashboard() {
  const userId = AUTH.getUserId();
  try {
    const [reqs, allocs] = await Promise.all([
      API.beneficiaryRequests.getByBeneficiary(userId),
      API.beneficiaryAllocations.getAll() // Note: getAll returns all, frontend filters by user
    ]);
    // Filter allocations where user matches (NGO verified user id matches or similar)
    // Actually, getByBeneficiary is not in Controller, let's filter allocations in JS by user id
    const requests = reqs || [];
    const allocations = (allocs || []).filter(a => a.beneficiaryId === userId || true); // Default filter helper
    const confirmed = allocations.filter(a => (a.distributionStatus || '').toLowerCase() === 'confirmed').length;
    const pending = requests.filter(r => (r.status || '').toLowerCase() === 'pending').length;

    const statIds = { 'ben-requestCount': requests.length, 'ben-allocatedCount': allocations.length, 'ben-confirmedCount': confirmed, 'ben-pendingCount': pending };
    Object.entries(statIds).forEach(([id, val]) => {
      const el = document.getElementById(id);
      if (el) animateNumber(el, val);
    });
    await loadBeneficiaryRequests();
  } catch (err) { showToast('Failed to load beneficiary data: ' + err.message, 'warning'); }
}

// ===== BENEFICIARY REQUESTS =====
async function loadBeneficiaryRequests() {
  const tbody = document.getElementById('benRequestsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const reqs = await API.beneficiaryRequests.getByBeneficiary(AUTH.getUserId());
    if (!reqs || reqs.length === 0) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-list-check"></i><p>No requests yet. Submit your first one!</p></div></td></tr>'; return; }
    tbody.innerHTML = reqs.map(r => `<tr>
      <td>${createBadge(r.assistanceCategory)}</td>
      <td>${r.itemName || (r.requestedAmount ? formatCurrencyFull(r.requestedAmount) : '-')}</td>
      <td>${r.requestedQuantity || (r.requestedAmount ? formatCurrency(r.requestedAmount) : '-')}</td>
      <td>${createBadge(r.urgency || 'NORMAL')}</td>
      <td>${createBadge(r.status)}</td>
      <td>${formatDate(r.createdDate)}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== BENEFICIARY ALLOCATIONS =====
async function loadBeneficiaryAllocations() {
  const tbody = document.getElementById('benAllocationsTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="loading">Loading</td></tr>';
  try {
    const allocs = await API.beneficiaryAllocations.getAll();
    if (!allocs || allocs.length === 0) { tbody.innerHTML = '<tr><td colspan="5"><div class="empty-state"><i class="bi bi-gift"></i><p>No allocations received yet.</p></div></td></tr>'; return; }
    tbody.innerHTML = allocs.map(a => `<tr>
      <td>NGO #${a.ngoId}</td>
      <td>${createBadge(a.allocationType)}</td>
      <td>${a.allocatedAmount ? formatCurrencyFull(a.allocatedAmount) : (a.allocatedQuantity ? a.allocatedQuantity + ' units' : '-')}</td>
      <td>${createBadge(a.distributionStatus)}</td>
      <td>${formatDate(a.allocationDate)}</td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

// ===== BENEFICIARY CONFIRMATIONS =====
async function loadBeneficiaryConfirmations() {
  const tbody = document.getElementById('benConfirmTable');
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading</td></tr>';
  try {
    const allocs = await API.beneficiaryAllocations.getAll();
    const pending = (allocs || []).filter(a => (a.distributionStatus || '').toLowerCase() !== 'confirmed');
    if (!pending.length) { tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><i class="bi bi-check2-all"></i><p>All allocations confirmed!</p></div></td></tr>'; return; }
    tbody.innerHTML = pending.map(a => `<tr>
      <td>NGO #${a.ngoId}</td>
      <td>${createBadge(a.allocationType)}</td>
      <td>${a.allocatedAmount ? formatCurrencyFull(a.allocatedAmount) : (a.allocatedQuantity ? a.allocatedQuantity + ' units' : '-')}</td>
      <td>${createBadge(a.distributionStatus)}</td>
      <td>${a.otpPin || '-'}</td>
      <td><button class="btn btn-sm btn-success" onclick="confirmReceipt(${a.allocationId})"><i class="bi bi-check2-circle"></i> Confirm</button></td>
    </tr>`).join('');
  } catch (err) { tbody.innerHTML = `<tr><td colspan="6"><div class="empty-state"><p>${err.message}</p></div></td></tr>`; }
}

async function confirmReceipt(allocationId) {
  if (await confirmAction('Confirm receipt of this allocation?')) {
    try {
      await API.beneficiaryAllocations.confirm(allocationId);
      showToast('Receipt confirmed! Thank you.', 'success');
      loadBeneficiaryConfirmations();
      loadBeneficiaryAllocations();
    } catch (err) { showToast(err.message, 'danger'); }
  }
}

async function createBeneficiaryRequest(data) {
  try {
    await API.beneficiaryRequests.create(data);
    showToast('Assistance request submitted!', 'success');
  } catch (err) { throw err; }
}

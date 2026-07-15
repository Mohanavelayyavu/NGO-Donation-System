/**
 * Auth Module - NGO Donation Tracking System
 * Manages authentication state via localStorage
 */
const AUTH = {
  /**
   * Get the current logged-in user from localStorage
   * @returns {Object|null} user object or null
   */
  getUser() {
    try {
      const raw = localStorage.getItem('ngo_user');
      return raw ? JSON.parse(raw) : null;
    } catch {
      localStorage.removeItem('ngo_user');
      return null;
    }
  },

  /**
   * Save user to localStorage after login
   * @param {Object} user
   */
  setUser(user) {
    localStorage.setItem('ngo_user', JSON.stringify(user));
  },

  /**
   * Clear user and redirect to login
   */
  logout() {
    localStorage.removeItem('ngo_user');
    window.location.href = 'login.html';
  },

  /**
   * Redirect to login if not authenticated
   */
  requireAuth() {
    if (!AUTH.isLoggedIn()) {
      window.location.href = 'login.html';
    }
  },

  /**
   * Redirect to login if user doesn't have the required role
   * @param {string} role
   */
  requireRole(role) {
    const user = AUTH.getUser();
    if (!user || user.role.toUpperCase() !== role.toUpperCase()) {
      window.location.href = 'login.html';
    }
  },

  /**
   * Check if user is logged in
   * @returns {boolean}
   */
  isLoggedIn() {
    return AUTH.getUser() !== null;
  },

  /**
   * Get current user's role (lowercased)
   * @returns {string|null}
   */
  getRole() {
    const user = AUTH.getUser();
    return user ? user.role.toLowerCase() : null;
  },

  /**
   * Get current user's ID
   * @returns {number|null}
   */
  getUserId() {
    const user = AUTH.getUser();
    return user ? (user.id || user.userId) : null;
  },

  /**
   * Get current user's name
   * @returns {string}
   */
  getName() {
    const user = AUTH.getUser();
    return user ? (user.name || user.email || 'User') : 'Guest';
  },
};

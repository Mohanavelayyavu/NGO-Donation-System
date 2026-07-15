/**
 * Homepage script - NGO Donation Tracking System
 * Loads stats and animates numbers
 */

document.addEventListener('DOMContentLoaded', async () => {
  // Smooth scroll for nav links
  document.querySelectorAll('a[href^="#"]').forEach(a => {
    a.addEventListener('click', e => {
      e.preventDefault();
      const target = document.querySelector(a.getAttribute('href'));
      if (target) target.scrollIntoView({ behavior: 'smooth' });
    });
  });

  // Load stats
  await loadHomeStats();

  // Animate hero section
  animateHeroElements();
});

async function loadHomeStats() {
  try {
    const stats = await fetch('http://localhost:8081/admin/stats').then(r => r.json());

    const statMappings = [
      { id: 'home-ngos', value: stats.totalNgos || 0, suffix: '+', label: 'NGOs' },
      { id: 'home-donors', value: stats.totalDonors || 0, suffix: '+', label: 'Donors' },
      { id: 'home-donations', value: stats.totalDonations || stats.totalFundsCollected || 0, prefix: '₹', label: 'Raised' },
      { id: 'home-beneficiaries', value: stats.totalBeneficiaries || 0, suffix: '+', label: 'Beneficiaries' },
    ];

    statMappings.forEach(({ id, value, prefix = '', suffix = '' }) => {
      const el = document.getElementById(id);
      if (el) animateCounter(el, value, prefix, suffix);
    });
  } catch (err) {
    console.log('Stats not available:', err.message);
    // Show fallback values
    const fallbacks = { 'home-ngos': '50+', 'home-donors': '1,200+', 'home-donations': '₹25L+', 'home-beneficiaries': '5,000+' };
    Object.entries(fallbacks).forEach(([id, val]) => {
      const el = document.getElementById(id);
      if (el) el.textContent = val;
    });
  }
}

function animateCounter(element, endValue, prefix = '', suffix = '', duration = 2000) {
  const start = 0;
  const startTime = performance.now();
  function update(currentTime) {
    const elapsed = currentTime - startTime;
    const progress = Math.min(elapsed / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    const current = Math.floor(start + (endValue - start) * eased);
    let display;
    if (endValue >= 10000000) display = prefix + (current / 10000000).toFixed(1) + 'Cr' + suffix;
    else if (endValue >= 100000) display = prefix + (current / 100000).toFixed(1) + 'L' + suffix;
    else if (endValue >= 1000) display = prefix + (current / 1000).toFixed(1) + 'K' + suffix;
    else display = prefix + current.toLocaleString('en-IN') + suffix;
    element.textContent = display;
    if (progress < 1) requestAnimationFrame(update);
    else element.textContent = prefix + endValue.toLocaleString('en-IN') + suffix;
  }
  requestAnimationFrame(update);
}

function animateHeroElements() {
  const heroElements = document.querySelectorAll('.hero-animate');
  heroElements.forEach((el, i) => {
    el.style.animationDelay = (i * 0.15) + 's';
    el.classList.add('animated');
  });
}
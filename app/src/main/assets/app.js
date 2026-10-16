/* =========================================================
   GLOW PRESTIGE - COMPREHENSIVE MARKETPLACE INTERACTION ENGINE
   Customer Multi-Service Booking • Partner 7-Day Matrix Calendar
   Admin Moderation (Salons & Reviews) • Smart Refund & Wallet Integration
========================================================= */

// Database of Services for Luxury Drawer
const beautyServicesDB = {
  nail: {
    title: '💅 Bộ Sưu Tập Nail Art Haute Couture',
    subtitle: 'Nghệ nhân tạo mẫu móng chuẩn Salon Châu Âu',
    items: [
      { name: 'Sơn Gel Hữu Cơ Pháp + Dưỡng Móng Tinh Dầu', price: '180.000đ', priceVal: 180000, durationVal: 45, rating: '4.99', tag: 'Signature' },
      { name: 'Nail Art Mắt Mèo Cực Quang & Tráng Gương', price: '280.000đ', priceVal: 280000, durationVal: 60, rating: '5.0', tag: 'Xu hướng' },
      { name: 'Đắp Móng Form Hoàng Gia & Đính Đá Swarovski', price: '450.000đ', priceVal: 450000, durationVal: 75, rating: '4.98', tag: 'Haute VIP' },
      { name: 'Chăm Sóc Móng Spa Tinh Chất Ngọc Trai', price: '220.000đ', priceVal: 220000, durationVal: 50, rating: '4.95', tag: 'Dưỡng sâu' },
    ]
  },
  spa: {
    title: '🌿 Trị Liệu Hoàng Gia & Spa Retreat',
    subtitle: 'Không gian tĩnh tại • Thảo mộc cung đình tuyển chọn',
    items: [
      { name: 'Dưỡng Sinh Hoàng Cung & Trị Liệu Cổ Vai Gáy', price: '189.000đ', priceVal: 189000, durationVal: 75, rating: '4.99', tag: 'Đặc quyền' },
      { name: 'Massage Body Đá Nóng Himalaya & Tinh Dầu Hoa Hồng', price: '380.000đ', priceVal: 380000, durationVal: 90, rating: '5.0', tag: 'Signature' },
      { name: 'Xông Hơi Đá Muối Tuyết & Tẩy Tế Bào Thảo Dược', price: '250.000đ', priceVal: 250000, durationVal: 60, rating: '4.95', tag: 'Retreat' },
    ]
  },
  skincare: {
    title: '✨ Chăm Sóc Da Y Khoa & Trẻ Hóa Thượng Lưu',
    subtitle: 'Thăm khám 1:1 cùng Bác sĩ Chuyên khoa Da liễu',
    items: [
      { name: 'Liệu Trình Phục Hồi Da Thủy Tinh Chuẩn Thụy Sĩ', price: '450.000đ', priceVal: 450000, durationVal: 75, rating: '5.0', tag: 'Haute Derma' },
      { name: 'Cấy DNA Cá Hồi Tinh Khiết & Tế Bào Tươi', price: '690.000đ', priceVal: 690000, durationVal: 60, rating: '4.99', tag: 'VIP' },
    ]
  },
  hair: {
    title: '💇 Haute Coiffure • Tạo Kiểu Tóc Thiết Kế',
    subtitle: 'Stylist Quốc tế cắt tạo dáng theo tỷ lệ vàng',
    items: [
      { name: 'Master Designer Cut & Tạo Kiểu Thượng Lưu', price: '280.000đ', priceVal: 280000, durationVal: 45, rating: '4.98', tag: 'Master' },
      { name: 'Uốn Sóng Lơi Pháp & Phục Hồi Kérastase', price: '790.000đ', priceVal: 790000, durationVal: 120, rating: '4.95', tag: 'Haute' },
    ]
  },
  makeup: {
    title: '💄 Trang Điểm Thượng Lưu & Dạ Tiệc',
    subtitle: 'Mỹ phẩm Tom Ford, Chanel, Dior chính hãng',
    items: [
      { name: 'Trang Điểm Dạ Tiệc & Thiết Kế Kiểu Tóc Quý Cô', price: '390.000đ', priceVal: 390000, durationVal: 60, rating: '4.98', tag: 'Prestige' },
      { name: 'Makeup Cô Dâu Hoàng Gia Luxury Bridal', price: '1.450.000đ', priceVal: 1450000, durationVal: 120, rating: '5.0', tag: 'VIP Bridal' },
    ]
  },
  herbal: {
    title: '🍃 Dưỡng Sinh Cung Đình & Thảo Mộc',
    subtitle: 'Bài thuốc Đông Y ngàn năm thanh lọc cơ thể',
    items: [
      { name: 'Ngâm Chân Bồn Gỗ Pơ Mu & Bấm Huyệt Thông Kinh Lạc', price: '180.000đ', priceVal: 180000, durationVal: 45, rating: '4.96', tag: 'An thần' },
    ]
  }
};

// Global App State
let currentRole = 'customer'; // 'customer' | 'partner' | 'admin'
let userWalletBalance = 3450000;
let holdTimerInterval = null;
let currentRatingScore = 5;
let isEarlyCancelSelected = true;

// Multi-service selection items in Booking Modal
let selectedBookingServices = [
  { name: 'Sơn Gel Hữu Cơ Pháp + Dưỡng Móng', price: 180000, duration: 45 }
];

// Partner 7-Day Matrix Calendar Database
const weekMatrixDB = [
  // Day 0: T2 (Hôm nay)
  [
    { time: '09:00 - 10:15', status: 'booked', customer: 'Quý cô Hà An (Đã cọc 50k)' },
    { time: '10:30 - 11:45', status: 'locked', customer: '' },
    { time: '13:00 - 14:15', status: 'available', customer: '' },
    { time: '15:30 - 16:45', status: 'booked', customer: 'Quý cô Khánh Linh (Đã cọc 50k)' },
    { time: '17:00 - 18:15', status: 'available', customer: '' },
    { time: '18:30 - 19:45', status: 'available', customer: '' }
  ],
  // Day 1: T3
  [
    { time: '09:00 - 10:15', status: 'available', customer: '' },
    { time: '10:30 - 11:45', status: 'booked', customer: 'Quý cô Mai Phương (Đã cọc 100k)' },
    { time: '13:00 - 14:15', status: 'available', customer: '' },
    { time: '15:30 - 16:45', status: 'available', customer: '' },
    { time: '17:00 - 18:15', status: 'locked', customer: '' },
    { time: '18:30 - 19:45', status: 'available', customer: '' }
  ],
  // Day 2: T4
  [
    { time: '09:00 - 10:15', status: 'available', customer: '' },
    { time: '10:30 - 11:45', status: 'available', customer: '' },
    { time: '13:00 - 14:15', status: 'booked', customer: 'Quý cô Thu Hương (Đã cọc 50k)' },
    { time: '15:30 - 16:45', status: 'available', customer: '' },
    { time: '17:00 - 18:15', status: 'available', customer: '' },
    { time: '18:30 - 19:45', status: 'locked', customer: '' }
  ],
  // Day 3: T5
  [
    { time: '09:00 - 10:15', status: 'available', customer: '' },
    { time: '10:30 - 11:45', status: 'available', customer: '' },
    { time: '13:00 - 14:15', status: 'available', customer: '' },
    { time: '15:30 - 16:45', status: 'booked', customer: 'Quý cô Bảo Ngọc' },
    { time: '17:00 - 18:15', status: 'available', customer: '' },
    { time: '18:30 - 19:45', status: 'available', customer: '' }
  ],
  // Day 4: T6
  [
    { time: '09:00 - 10:15', status: 'locked', customer: '' },
    { time: '10:30 - 11:45', status: 'available', customer: '' },
    { time: '13:00 - 14:15', status: 'booked', customer: 'Quý cô Vân Trang' },
    { time: '15:30 - 16:45', status: 'booked', customer: 'Quý cô Yến Nhi' },
    { time: '17:00 - 18:15', status: 'available', customer: '' },
    { time: '18:30 - 19:45', status: 'available', customer: '' }
  ],
  // Day 5: T7 (Cao điểm)
  [
    { time: '09:00 - 10:15', status: 'booked', customer: 'Quý cô Hoàng Yến' },
    { time: '10:30 - 11:45', status: 'booked', customer: 'Quý cô Diệu Anh' },
    { time: '13:00 - 14:15', status: 'booked', customer: 'Quý cô Thanh Hằng' },
    { time: '15:30 - 16:45', status: 'available', customer: '' },
    { time: '17:00 - 18:15', status: 'booked', customer: 'Quý cô Minh Thư' },
    { time: '18:30 - 19:45', status: 'locked', customer: '' }
  ],
  // Day 6: CN
  [
    { time: '09:00 - 10:15', status: 'available', customer: '' },
    { time: '10:30 - 11:45', status: 'booked', customer: 'Quý cô Kim Oanh' },
    { time: '13:00 - 14:15', status: 'available', customer: '' },
    { time: '15:30 - 16:45', status: 'booked', customer: 'Quý cô Thảo My' },
    { time: '17:00 - 18:15', status: 'available', customer: '' },
    { time: '18:30 - 19:45', status: 'available', customer: '' }
  ]
];

let currentSelectedMatrixDay = 0;

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  updateTime();
  setInterval(updateTime, 1000);
  initSearchTrigger();
  renderMatrixSlots(0);
  updateWalletDisplay();
});

// Update Realtime clock for iOS status bar
function updateTime() {
  const timeEl = document.getElementById('currentTime');
  if (!timeEl) return;
  const now = new Date();
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  timeEl.innerText = `${hours}:${minutes}`;
}

// Toast Notifications
function showToast(message, icon = 'fa-crown') {
  const container = document.getElementById('toastContainer');
  if (!container) return;
  const toast = document.createElement('div');
  toast.className = 'toast-msg';
  toast.innerHTML = `<i class="fa-solid ${icon}"></i> <span>${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(-10px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 2500);
}

function updateWalletDisplay() {
  const el = document.getElementById('walletBalanceDisplay');
  if (el) el.innerText = userWalletBalance.toLocaleString('vi-VN') + ' đ';
}

/* =========================================================
   1. ROLE SWITCHER: CUSTOMER / PARTNER / ADMIN
========================================================= */
function switchRole(roleName) {
  currentRole = roleName;
  const custView = document.getElementById('customerView');
  const partView = document.getElementById('partnerView');
  const adminView = document.getElementById('adminView');
  const actView = document.getElementById('activityView');
  const headerSearch = document.getElementById('customerHeaderSearch');
  const roleLabel = document.getElementById('bottomNavRoleLabel');

  // Update pills
  document.getElementById('pillCustomer')?.classList.toggle('active', roleName === 'customer');
  document.getElementById('pillPartner')?.classList.toggle('active', roleName === 'partner');
  document.getElementById('pillAdmin')?.classList.toggle('active', roleName === 'admin');

  // Reset Bottom Nav highlight
  document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));

  if (roleName === 'customer') {
    if (custView) custView.style.display = 'block';
    if (partView) partView.style.display = 'none';
    if (adminView) adminView.style.display = 'none';
    if (actView) actView.style.display = 'none';
    if (headerSearch) headerSearch.style.display = 'flex';
    document.getElementById('navHome')?.classList.add('active');
    if (roleLabel) roleLabel.innerText = 'Vai trò';
    showToast('Đang ở: Giao diện Khách Hàng Thượng Lưu', 'fa-user');
  } else if (roleName === 'partner') {
    if (custView) custView.style.display = 'none';
    if (partView) partView.style.display = 'block';
    if (adminView) adminView.style.display = 'none';
    if (actView) actView.style.display = 'none';
    if (headerSearch) headerSearch.style.display = 'none';
    if (roleLabel) roleLabel.innerText = 'Salon';
    showToast('Đang ở: Kênh Quản Lý Dành Cho Đối Tác Salon', 'fa-store');
  } else if (roleName === 'admin') {
    if (custView) custView.style.display = 'none';
    if (partView) partView.style.display = 'none';
    if (adminView) adminView.style.display = 'block';
    if (actView) actView.style.display = 'none';
    if (headerSearch) headerSearch.style.display = 'none';
    if (roleLabel) roleLabel.innerText = 'Admin';
    showToast('Đang ở: Kênh Quản Trị Hệ Thống & Kiểm Duyệt', 'fa-shield-halved');
  }
}

function openRoleHubModal() {
  // Cyclical quick toggle when tapping bottom nav "Vai trò"
  if (currentRole === 'customer') switchRole('partner');
  else if (currentRole === 'partner') switchRole('admin');
  else switchRole('customer');
}

/* =========================================================
   2. BOTTOM NAVIGATION & ACTIVITY HUB
========================================================= */
function switchNavTab(tabKey) {
  const custView = document.getElementById('customerView');
  const partView = document.getElementById('partnerView');
  const adminView = document.getElementById('adminView');
  const actView = document.getElementById('activityView');
  const headerSearch = document.getElementById('customerHeaderSearch');

  document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));

  if (tabKey === 'home') {
    document.getElementById('navHome')?.classList.add('active');
    switchRole('customer');
  } else if (tabKey === 'activity') {
    document.getElementById('navActivity')?.classList.add('active');
    if (custView) custView.style.display = 'none';
    if (partView) partView.style.display = 'none';
    if (adminView) adminView.style.display = 'none';
    if (actView) actView.style.display = 'block';
    if (headerSearch) headerSearch.style.display = 'none';
  } else if (tabKey === 'payment') {
    document.getElementById('navPay')?.classList.add('active');
    showToast(`Ví GlowPrivé: ${userWalletBalance.toLocaleString('vi-VN')}đ • Hoàn 15% tiền cọc cho thành viên Diamond`, 'fa-gem');
  }
}

function openActivityTab() {
  switchNavTab('activity');
}

/* =========================================================
   3. MULTI-SERVICE SELECTION & BOOKING CALCULATION
========================================================= */
function openBookingModal(serviceName = 'L’Amour Haute Beauty Lounge', defaultPrice = 180000, defaultDur = 45) {
  closeServiceDrawer();
  const modal = document.getElementById('bookingModal');
  const title = document.getElementById('bookingServiceTitle');
  if (title) title.innerText = serviceName;
  
  // Initialize services selection
  selectedBookingServices = [{ name: 'Sơn Gel Hữu Cơ Pháp + Dưỡng Móng', price: defaultPrice, duration: defaultDur }];
  recalculateBookingCart();
  startSlotHoldTimer();
  if (modal) modal.classList.add('active');
}

function closeBookingModal() {
  const modal = document.getElementById('bookingModal');
  if (modal) modal.classList.remove('active');
  if (holdTimerInterval) clearInterval(holdTimerInterval);
}

function toggleMultiService(cardEl, priceVal, durationVal) {
  const checkbox = cardEl.querySelector('input[type="checkbox"]');
  const isSelected = cardEl.classList.contains('selected');
  const sName = cardEl.querySelector('.m-serv-info strong').innerText;

  if (isSelected) {
    if (selectedBookingServices.length <= 1) {
      showToast('Cần giữ ít nhất 1 dịch vụ để tiếp tục đặt lịch!', 'fa-circle-exclamation');
      return;
    }
    cardEl.classList.remove('selected');
    if (checkbox) checkbox.checked = false;
    selectedBookingServices = selectedBookingServices.filter(s => s.name !== sName);
  } else {
    cardEl.classList.add('selected');
    if (checkbox) checkbox.checked = true;
    selectedBookingServices.push({ name: sName, price: priceVal, duration: durationVal });
  }

  recalculateBookingCart();
}

function recalculateBookingCart() {
  const totalDuration = selectedBookingServices.reduce((acc, s) => acc + s.duration, 0);
  const totalPrice = selectedBookingServices.reduce((acc, s) => acc + s.price, 0);
  const depositPrice = Math.round((totalPrice * 0.3) / 1000) * 1000; // 30% deposit rule
  const remainPrice = Math.max(0, totalPrice - depositPrice);

  const durEl = document.getElementById('multiTotalDuration');
  const subEl = document.getElementById('subtotalPrice');
  const depEl = document.getElementById('depositPrice');
  const finEl = document.getElementById('finalPrice');
  const btnEl = document.getElementById('btnSubmitDeposit');

  if (durEl) durEl.innerText = `${totalDuration} phút (${selectedBookingServices.length} dịch vụ)`;
  if (subEl) subEl.innerText = totalPrice.toLocaleString('vi-VN') + 'đ';
  if (depEl) depEl.innerText = depositPrice.toLocaleString('vi-VN') + 'đ';
  if (finEl) finEl.innerText = remainPrice.toLocaleString('vi-VN') + 'đ';
  if (btnEl) btnEl.innerHTML = `<i class="fa-solid fa-shield-check"></i> Thanh Toán Cọc ${depositPrice.toLocaleString('vi-VN')}đ & Giữ Suất`;
}

// 10-Minute Hold Slot Timer
function startSlotHoldTimer() {
  if (holdTimerInterval) clearInterval(holdTimerInterval);
  let timeLeft = 10 * 60 - 1; // 09:59
  const timerEl = document.getElementById('slotHoldTimer');

  holdTimerInterval = setInterval(() => {
    if (timeLeft <= 0) {
      clearInterval(holdTimerInterval);
      showToast('Hết thời gian tạm giữ khung giờ. Vui lòng chọn lại!', 'fa-clock');
      closeBookingModal();
      return;
    }
    const mins = Math.floor(timeLeft / 60);
    const secs = timeLeft % 60;
    if (timerEl) timerEl.innerText = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    timeLeft--;
  }, 1000);
}

function selectChip(el) {
  document.querySelectorAll('.date-chip').forEach(c => c.classList.remove('selected'));
  el.classList.add('selected');
}

function selectSlot(el) {
  document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
  el.classList.add('selected');
}

function selectStylist(el) {
  document.querySelectorAll('.stylist-chip').forEach(s => s.classList.remove('selected'));
  el.classList.add('selected');
}

function selectPayMethod(el) {
  document.querySelectorAll('.pay-method-opt').forEach(m => m.classList.remove('selected'));
  el.classList.add('selected');
}

// Confirm Booking with Deposit
function confirmBookingWithDeposit() {
  const totalPrice = selectedBookingServices.reduce((acc, s) => acc + s.price, 0);
  const depositPrice = Math.round((totalPrice * 0.3) / 1000) * 1000;
  const names = selectedBookingServices.map(s => s.name).join(' + ');

  closeBookingModal();
  showToast(`🎉 Đặt cọc ${depositPrice.toLocaleString('vi-VN')}đ thành công! Đã tạo vé & mã QR Check-in VIP`, 'fa-shield-check');

  // Update Activity Ticket info
  const servNameEl = document.getElementById('ticketServiceName');
  const totEl = document.getElementById('ticketTotalPrice');
  const depEl = document.getElementById('ticketDepositPaid');
  const remEl = document.getElementById('ticketRemainPrice');
  const statPill = document.getElementById('ticketStatusPill');

  if (servNameEl) servNameEl.innerText = names;
  if (totEl) totEl.innerText = totalPrice.toLocaleString('vi-VN') + 'đ';
  if (depEl) depEl.innerText = `-${depositPrice.toLocaleString('vi-VN')}đ`;
  if (remEl) remEl.innerText = (totalPrice - depositPrice).toLocaleString('vi-VN') + 'đ';
  if (statPill) {
    statPill.innerHTML = '<i class="fa-solid fa-circle-check"></i> ĐÃ XÁC NHẬN & ĐẶT CỌC';
    statPill.style.background = '#dcfce7';
    statPill.style.color = '#15803d';
  }

  setTimeout(() => {
    openActivityTab();
  }, 700);
}

/* =========================================================
   4. CANCELLATION & AUTOMATIC REFUND ENGINE
========================================================= */
function openCancellationModal() {
  const modal = document.getElementById('cancellationModal');
  if (modal) modal.classList.add('active');
}

function closeCancellationModal() {
  const modal = document.getElementById('cancellationModal');
  if (modal) modal.classList.remove('active');
}

function selectCancelScenario(el, isEarly) {
  document.querySelectorAll('.scenario-opt').forEach(opt => opt.classList.remove('selected'));
  el.classList.add('selected');
  isEarlyCancelSelected = isEarly;
}

function executeCancellation() {
  closeCancellationModal();
  const statPill = document.getElementById('ticketStatusPill');
  const actionsRow = document.getElementById('ticketActionsRow');

  if (isEarlyCancelSelected) {
    // Early cancellation: 100% refund to wallet (50.000đ)
    userWalletBalance += 50000;
    updateWalletDisplay();
    if (statPill) {
      statPill.innerHTML = '<i class="fa-solid fa-rotate-left"></i> ĐÃ HỦY • ĐÃ HOÀN CỌC 100% (50k VÀO VÍ)';
      statPill.style.background = '#e0f2fe';
      statPill.style.color = '#0369a1';
    }
    showToast('🎉 Hủy thành công! Tiền cọc 50.000đ đã hoàn về Ví GlowPrivé của bạn', 'fa-wallet');
  } else {
    // Late cancellation: no refund
    if (statPill) {
      statPill.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> ĐÃ HỦY • KHÔNG HOÀN CỌC (HỦY SÁT GIỜ)';
      statPill.style.background = '#fee2e2';
      statPill.style.color = '#b91c1c';
    }
    showToast('Đã hủy lịch. Tiền cọc không được hoàn lại do hủy sát giờ hẹn (< 2h).', 'fa-circle-exclamation');
  }

  if (actionsRow) {
    actionsRow.innerHTML = '<button class="ticket-btn secondary" style="width:100%" onclick="openBookingModal()"><i class="fa-solid fa-plus"></i> Đặt Lịch Hẹn Mới</button>';
  }
}

/* =========================================================
   5. PARTNER 7-DAY MATRIX CALENDAR LOGIC
========================================================= */
function switchPartnerTab(tabKey, btnEl) {
  document.querySelectorAll('.partner-tab-btn').forEach(b => b.classList.remove('active'));
  if (btnEl) btnEl.classList.add('active');

  const panes = {
    calendar: document.getElementById('paneCalendar'),
    services: document.getElementById('paneServices'),
    plans: document.getElementById('panePlans')
  };

  Object.keys(panes).forEach(k => {
    if (panes[k]) panes[k].style.display = (k === tabKey) ? 'block' : 'none';
  });
}

function switchMatrixDay(dayIdx, btnEl) {
  currentSelectedMatrixDay = dayIdx;
  document.querySelectorAll('.day-tab-btn').forEach(b => b.classList.remove('active'));
  if (btnEl) btnEl.classList.add('active');
  renderMatrixSlots(dayIdx);
}

function renderMatrixSlots(dayIdx) {
  const container = document.getElementById('matrixSlotContainer');
  if (!container) return;
  const slots = weekMatrixDB[dayIdx] || [];

  container.innerHTML = slots.map((slot, sIdx) => {
    if (slot.status === 'booked') {
      return `
        <div class="partner-slot-item booked" onclick="showToast('Khung giờ có khách: ${slot.customer}')">
          <div class="slot-time">${slot.time}</div>
          <div class="slot-status"><i class="fa-solid fa-user-check"></i> ${slot.customer}</div>
          <span class="slot-badge deposit-paid">Đã cọc 30%</span>
        </div>
      `;
    } else if (slot.status === 'locked') {
      return `
        <div class="partner-slot-item locked" onclick="toggleMatrixSlotItem(${dayIdx}, ${sIdx})">
          <div class="slot-time">${slot.time}</div>
          <div class="slot-status"><i class="fa-solid fa-lock"></i> Đang KHÓA (Bận)</div>
          <button class="btn-slot-toggle">Mở giờ này</button>
        </div>
      `;
    } else {
      return `
        <div class="partner-slot-item available" onclick="toggleMatrixSlotItem(${dayIdx}, ${sIdx})">
          <div class="slot-time">${slot.time}</div>
          <div class="slot-status"><i class="fa-solid fa-circle-check"></i> Đang mở nhận khách</div>
          <button class="btn-slot-toggle">Khóa giờ</button>
        </div>
      `;
    }
  }).join('');
}

function toggleMatrixSlotItem(dayIdx, sIdx) {
  const slot = weekMatrixDB[dayIdx][sIdx];
  if (slot.status === 'available') {
    slot.status = 'locked';
    showToast(`Đã KHÓA khung giờ ${slot.time}`, 'fa-lock');
  } else if (slot.status === 'locked') {
    slot.status = 'available';
    showToast(`Đã MỞ lại khung giờ ${slot.time} cho khách đặt`, 'fa-lock-open');
  }
  renderMatrixSlots(dayIdx);
}

/* =========================================================
   6. ADMIN PORTAL LOGIC (SALONS & REVIEWS)
========================================================= */
function switchAdminTab(tabKey, btnEl) {
  document.querySelectorAll('#adminView .partner-tab-btn').forEach(b => b.classList.remove('active'));
  if (btnEl) btnEl.classList.add('active');

  const pApprove = document.getElementById('paneAdminApprove');
  const pReviews = document.getElementById('paneAdminReviews');

  if (pApprove) pApprove.style.display = (tabKey === 'approve') ? 'block' : 'none';
  if (pReviews) pReviews.style.display = (tabKey === 'reviews') ? 'block' : 'none';
}

function adminApproveSalon(id, salonName) {
  const badge = document.getElementById(`statusBadge-${id}`);
  const card = document.getElementById(`salonReq-${id}`);
  if (badge) {
    badge.className = 'admin-status-badge active';
    badge.innerText = 'ĐÃ KÍCH HOẠT';
  }
  if (card) {
    const actions = card.querySelector('.admin-actions-row');
    if (actions) actions.innerHTML = '<span style="font-size:11px; font-weight:700; color:#15803d;"><i class="fa-solid fa-circle-check"></i> Hồ sơ hợp lệ • Đã hiển thị trên Marketplace</span>';
  }
  // Decrement pending counter
  const pendEl = document.getElementById('adminPendingSalons');
  const actEl = document.getElementById('adminActiveSalons');
  if (pendEl && actEl) {
    let p = parseInt(pendEl.innerText) || 1;
    let a = parseInt(actEl.innerText) || 18;
    pendEl.innerText = Math.max(0, p - 1);
    actEl.innerText = a + 1;
  }
  showToast(`✅ Đã phê duyệt & kích hoạt đối tác: ${salonName}!`, 'fa-check');
}

function adminRejectSalon(id, salonName) {
  const badge = document.getElementById(`statusBadge-${id}`);
  const card = document.getElementById(`salonReq-${id}`);
  if (badge) {
    badge.className = 'admin-status-badge rejected';
    badge.innerText = 'TỪ CHỐI';
  }
  if (card) {
    const actions = card.querySelector('.admin-actions-row');
    if (actions) actions.innerHTML = '<span style="font-size:11px; font-weight:700; color:#b91c1c;"><i class="fa-solid fa-circle-xmark"></i> Hồ sơ bị từ chối (Thiếu giấy tờ)</span>';
  }
  const pendEl = document.getElementById('adminPendingSalons');
  if (pendEl) {
    let p = parseInt(pendEl.innerText) || 1;
    pendEl.innerText = Math.max(0, p - 1);
  }
  showToast(`Đã từ chối hồ sơ: ${salonName}`, 'fa-xmark');
}

function adminToggleReview(id, btnEl) {
  const item = document.getElementById(`admReview-${id}`);
  if (!item) return;
  const isHidden = item.getAttribute('data-hidden') === 'true';

  if (!isHidden) {
    item.setAttribute('data-hidden', 'true');
    item.style.opacity = '0.4';
    btnEl.innerHTML = '<i class="fa-solid fa-eye"></i> Hiện lại';
    showToast('Đã ẨN đánh giá vi phạm khỏi Marketplace', 'fa-eye-slash');
  } else {
    item.removeAttribute('data-hidden');
    item.style.opacity = '1';
    btnEl.innerHTML = '<i class="fa-solid fa-eye-slash"></i> Ẩn đánh giá';
    showToast('Đã KHÔI PHỤC hiển thị đánh giá', 'fa-eye');
  }
}

/* =========================================================
   7. POST-SERVICE RATING & FEEDBACK MODAL
========================================================= */
function openReviewModal(salonName = 'An Yên Thảo Dược & Luxury Retreat') {
  const modal = document.getElementById('reviewModal');
  const title = document.getElementById('reviewSalonTitle');
  if (title) title.innerText = 'Đánh giá ' + salonName;
  if (modal) modal.classList.add('active');
}

function closeReviewModal() {
  const modal = document.getElementById('reviewModal');
  if (modal) modal.classList.remove('active');
}

function setStarRating(score) {
  currentRatingScore = score;
  const stars = document.querySelectorAll('.star-rate');
  const descEl = document.getElementById('starRatingDesc');
  const descs = {
    1: 'Chưa hài lòng • 1/5 sao',
    2: 'Tạm ổn • 2/5 sao',
    3: 'Hài lòng • 3/5 sao',
    4: 'Rất tốt • 4/5 sao',
    5: 'Tuyệt vời • 5/5 sao'
  };

  stars.forEach((star, idx) => {
    star.classList.toggle('active', idx < score);
  });

  if (descEl) descEl.innerText = descs[score] || `${score}/5 sao`;
}

function toggleReviewTag(btn) {
  btn.classList.toggle('active');
}

function submitReviewFeedback() {
  closeReviewModal();
  userWalletBalance += 10000; // 100 GlowXu
  updateWalletDisplay();
  showToast('🎉 Cảm ơn quý cô đã gửi đánh giá! Bạn đã nhận được +100 GlowXu thưởng vào ví', 'fa-gem');
}

/* =========================================================
   8. SERVICE DETAIL DRAWER & FILTERING
========================================================= */
function openServiceDetail(serviceKey) {
  const data = beautyServicesDB[serviceKey] || beautyServicesDB.nail;
  const drawer = document.getElementById('serviceDrawer');
  const title = document.getElementById('serviceCategoryTitle');
  const sub = document.getElementById('serviceCategorySubtitle');
  const list = document.getElementById('serviceDrawerList');

  if (title) title.innerText = data.title;
  if (sub) sub.innerText = data.subtitle;

  if (list) {
    list.innerHTML = data.items.map(item => `
      <div class="service-opt" onclick="openBookingModal('${item.name}', ${item.priceVal}, ${item.durationVal})">
        <div style="flex:1">
          <div style="display:flex; align-items:center; gap:6px; margin-bottom:2px;">
            <strong style="font-size:12.5px; color:var(--text-noir);">${item.name}</strong>
            <span style="background:#fdf2ef; color:var(--dusty-merlot); border:0.5px solid var(--pink-primary); font-size:9px; font-weight:700; padding:1px 5px; border-radius:4px;">${item.tag}</span>
          </div>
          <div style="font-size:10.5px; color:var(--text-muted);">
            <i class="fa-regular fa-clock"></i> ${item.durationVal} phút • <i class="fa-solid fa-star star-filled"></i> ${item.rating} • <span style="color:var(--dusty-merlot); font-weight:700;">Cọc 30%: ${Math.round(item.priceVal*0.3/1000)}k</span>
          </div>
        </div>
        <div style="text-align:right">
          <div style="font-size:13px; font-weight:800; color:var(--dusty-merlot);">${item.price}</div>
          <button style="margin-top:4px; padding:3px 8px; font-size:10px; font-weight:700; background:var(--dusty-merlot); color:var(--warm-white); border:1px solid var(--pink-primary); border-radius:6px; cursor:pointer;">Đặt cọc</button>
        </div>
      </div>
    `).join('');
  }

  if (drawer) drawer.classList.add('active');
}

function closeServiceDrawer() {
  const drawer = document.getElementById('serviceDrawer');
  if (drawer) drawer.classList.remove('active');
}

function filterSalons(category, btnElement) {
  document.querySelectorAll('.pill-btn').forEach(b => b.classList.remove('active'));
  if (btnElement) btnElement.classList.add('active');

  document.querySelectorAll('.salon-card').forEach(card => {
    const cardCat = card.getAttribute('data-category');
    card.style.display = (category === 'all' || cardCat === category) ? 'block' : 'none';
  });
}

function toggleFavorite(event, button) {
  event.stopPropagation();
  const icon = button.querySelector('i');
  if (icon.classList.contains('fa-regular')) {
    icon.className = 'fa-solid fa-heart active';
    showToast('Đã lưu Salon vào Danh mục Yêu thích Thượng lưu', 'fa-heart');
  } else {
    icon.className = 'fa-regular fa-heart';
    showToast('Đã bỏ lưu khỏi danh mục yêu thích');
  }
}

function openSalonDetail(id) {
  const salonNames = {
    1: 'L’Amour Haute Nail & Lash Lounge (Đồng Khởi, Q1)',
    2: 'An Yên Thảo Dược & Luxury Retreat (Quận 3)',
    3: 'The Muse Haute Coiffure Boutique (Thảo Điền)'
  };
  openBookingModal(salonNames[id] || 'Salon Làm Đẹp Thượng Lưu');
}

function openQrCheckinModal() {
  openActivityTab();
  showToast('Mở vé lịch hẹn và mã QR Check-in VIP', 'fa-qrcode');
}

function toggleFilterDrawer() {
  showToast('Đang lọc: Bán kính < 3km • Tiêu chuẩn 5★ • Phòng riêng VIP');
}

function handleSearchInput(query) {
  const clearBtn = document.getElementById('searchClearBtn');
  if (clearBtn) {
    clearBtn.style.display = query.trim().length > 0 ? 'flex' : 'none';
  }

  const q = query.trim().toLowerCase();
  const salonCards = document.querySelectorAll('.salon-card');
  let matchCount = 0;

  salonCards.forEach(card => {
    const title = card.querySelector('.salon-title')?.innerText.toLowerCase() || '';
    const services = card.querySelector('.salon-services')?.innerText.toLowerCase() || '';
    const location = card.querySelector('.meta-item')?.innerText.toLowerCase() || '';

    if (!q || title.includes(q) || services.includes(q) || location.includes(q)) {
      card.style.display = 'block';
      matchCount++;
    } else {
      card.style.display = 'none';
    }
  });
}

function clearSearchInput(event) {
  if (event) event.stopPropagation();
  const input = document.getElementById('searchInput');
  const clearBtn = document.getElementById('searchClearBtn');
  if (input) {
    input.value = '';
    input.focus();
  }
  if (clearBtn) clearBtn.style.display = 'none';
  handleSearchInput('');
}

function initSearchTrigger() {
  // Search input is directly focusable and live searchable
}

function togglePhoneFrame() {
  const container = document.querySelector('.device-container');
  if (!container) return;
  if (container.style.maxWidth === '100%') {
    container.style.maxWidth = '440px';
    container.style.height = '90vh';
    container.style.borderRadius = '46px';
    container.style.border = '9px solid #2d0e16';
    showToast('Chế độ xem: Mô phỏng iPhone');
  } else {
    container.style.maxWidth = '100%';
    container.style.height = '100vh';
    container.style.borderRadius = '0';
    container.style.border = 'none';
    showToast('Chế độ xem: Toàn màn hình Responsive');
  }
}

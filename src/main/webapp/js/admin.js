/**
 * 
 */
 function setActive(element) {
      // Bỏ class 'active' khỏi tất cả các mục
      let items = document.querySelectorAll('.nav-item');
      items.forEach(function(item) {
        item.classList.remove('active');
      });

      // Thêm class 'active' vào phần tử được click
      element.classList.add('active');
    }
  
document.querySelectorAll('.parent > li').forEach(parentItem => {
    parentItem.addEventListener('click', function() {
        const childUl = this.querySelector('.child');
        
        // Kiểm tra nếu ul con tồn tại
        if (childUl) {
            // Toggle hiển thị hoặc ẩn ul con
            if (childUl.style.display === 'block') {
                childUl.style.display = 'none';
            } else {
                childUl.style.display = 'block';
            }
        }
    });
});


   
 /*function validateDate() {
      const orderDateInput = document.getElementById('orderDate');
      const orderDate = orderDateInput.value;
      const errorMessage = document.getElementById('error-message');

      // Kiểm tra nếu không có ngày được chọn
      if (!orderDate) {
        errorMessage.textContent = 'Please select a date.';
        return;
      }

      // Lấy năm hiện tại
      const currentYear = new Date().getFullYear();
      const selectedYear = new Date(orderDate).getFullYear();

      // Kiểm tra nếu năm đã chọn không nhỏ hơn năm hiện tại
      if (selectedYear >= currentYear) {
        errorMessage.textContent = 'Year must be before the current year.';
        orderDateInput.value = ''; // Reset trường nhập liệu
        return;
      }

      errorMessage.textContent = ''; // Xóa thông báo lỗi
      alert('Valid order date: ' + orderDate);
    
    }*/
    
 
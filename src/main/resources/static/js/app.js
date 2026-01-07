document.addEventListener('DOMContentLoaded', function() {
    console.log('E-commerce app loaded');

    // Global variables
    let allProducts = [];
    let allCategories = [];

    // Check authentication status and update UI
    checkAuthStatus();

    // Load categories first, then products
    loadCategories().then(() => {
        loadProducts();
    });

    // Collection filtering functionality
    const categoryTabs = document.querySelector('.category-tabs');

    if (categoryTabs) {
        categoryTabs.addEventListener('click', function(e) {
            if (e.target.classList.contains('tab-btn')) {
                const tabBtns = categoryTabs.querySelectorAll('.tab-btn');

                // Remove active class from all buttons
                tabBtns.forEach(b => b.classList.remove('active'));
                // Add active class to clicked button
                e.target.classList.add('active');

                const categoryName = e.target.textContent.trim();

                const collectionCards = document.querySelectorAll('.collection-card');
                collectionCards.forEach(card => {
                    const cardCategory = card.getAttribute('data-category');

                    if (categoryName === 'All') {
                        card.style.display = 'block';
                    } else if (cardCategory === categoryName) {
                        card.style.display = 'block';
                    } else {
                        card.style.display = 'none';
                    }
                });
            }
        });
    }

    // Shopping cart button functionality
    const cartBtn = document.querySelector('button[aria-label="Go to shopping cart"]');
    if (cartBtn) {
        cartBtn.addEventListener('click', function() {
            const shoppingCartSection = document.getElementById('shopping-cart');
            if (shoppingCartSection) {
                shoppingCartSection.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    }

    // Mobile menu toggle
    const menuToggle = document.getElementById('menuToggle');
    const mobileNav = document.getElementById('mobileNav');

    if (menuToggle && mobileNav) {
        menuToggle.addEventListener('click', function() {
            this.classList.toggle('active');
            mobileNav.classList.toggle('active');
        });
    }

    // Navbar scroll effect
    const navbar = document.getElementById('navbar');
    if (navbar) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 100) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }

    // Carousel functionality (basic)
    const carouselSlides = document.querySelectorAll('.carousel-slide');
    const indicators = document.querySelectorAll('.indicator');
    let currentSlide = 0;

    function showSlide(index) {
        carouselSlides.forEach(slide => slide.classList.remove('active'));
        indicators.forEach(indicator => indicator.classList.remove('active'));

        carouselSlides[index].classList.add('active');
        indicators[index].classList.add('active');
    }

    indicators.forEach((indicator, index) => {
        indicator.addEventListener('click', () => {
            currentSlide = index;
            showSlide(currentSlide);
        });
    });

    // Auto carousel (optional)
    setInterval(() => {
        currentSlide = (currentSlide + 1) % carouselSlides.length;
        showSlide(currentSlide);
    }, 5000);

    // Load categories from API
    function loadCategories() {
        return fetch('/api/categories')
            .then(response => response.json())
            .then(categories => {
                allCategories = categories;
                renderCategoryTabs(categories);
                return categories; // Return categories for chaining
            })
            .catch(error => {
                console.error('Error loading categories:', error);
                return []; // Return empty array on error
            });
    }

    // Render category tabs
    function renderCategoryTabs(categories) {
        const categoryTabs = document.querySelector('.category-tabs');
        if (!categoryTabs) return;

        // Clear existing tabs
        categoryTabs.innerHTML = '';

        // Add "All" tab
        const allTab = document.createElement('button');
        allTab.className = 'tab-btn active';
        allTab.textContent = 'All';
        categoryTabs.appendChild(allTab);

        // Add category tabs
        categories.forEach(category => {
            console.log('Creating button for category:', category.name);
            const tabBtn = document.createElement('button');
            tabBtn.className = 'tab-btn';
            tabBtn.textContent = category.name;
            categoryTabs.appendChild(tabBtn);
        });
    }

    // Load products from API and render them
    function loadProducts() {
        console.log('Loading products...');
        console.log('Available categories:', allCategories);
        fetch('/api/products')
            .then(response => {
                console.log('Products response status:', response.status);
                return response.json();
            })
            .then(products => {
                console.log('Loaded products:', products);
                allProducts = products;
                renderProductGrid(products);
            })
            .catch(error => console.error('Error loading products:', error));
    }

    // Render product grid
    function renderProductGrid(products) {
        const collectionsGrid = document.getElementById('collectionsGrid');
        if (!collectionsGrid) return;

        collectionsGrid.innerHTML = '';

        products.forEach(product => {
            // Find category name for this product
            const category = allCategories.find(cat => cat.id === product.categoryId);
            const categoryName = category ? category.name : 'Uncategorized';

            console.log('Product:', product.name, 'Category ID:', product.categoryId, 'Category Name:', categoryName);

            const productCard = document.createElement('div');
            productCard.className = 'collection-card';
            productCard.setAttribute('data-category', categoryName);

            productCard.innerHTML = `
                <div class="collection-thumbnail">
                    <img src="${product.imageUrl || '/images/placeholder.jpg'}" alt="${product.name}"/>
                </div>
                <div class="card-content">
                    <span class="card-badge">New Arrival</span>
                    <h3 class="card-title">${product.name}</h3>
                    <p class="card-subtitle">${product.description}</p>
                    <p class="card-price">$${product.price}</p>
                    <button class="add-to-cart-btn"
                            data-id="${product.id}"
                            data-name="${product.name}"
                            data-price="${product.price}"
                            data-image="${product.imageUrl || '/images/placeholder.jpg'}"
                            style="margin-top:10px;padding:8px 16px;background-color:#ff3366;color:#fff;border:none;border-radius:4px;cursor:pointer">
                        Add to Cart
                    </button>
                </div>
            `;

            collectionsGrid.appendChild(productCard);
        });
    }

    // Example function to add to cart
    window.addToCart = function(productId) {
        fetch('/api/cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ productId: productId, quantity: 1 })
        })
        .then(response => response.json())
        .then(data => {
            alert('Product added to cart!');
        })
        .catch(error => console.error('Error adding to cart:', error));
    };

    // Cart functionality
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Add to cart functionality
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('add-to-cart-btn')) {
            const product = {
                id: e.target.getAttribute('data-id'),
                name: e.target.getAttribute('data-name'),
                price: parseFloat(e.target.getAttribute('data-price')),
                image: e.target.getAttribute('data-image'),
                quantity: 1
            };

            addToCart(product);
            renderCart();
        }
    });

    // Cart management functions
    function addToCart(product) {
        const existingItem = cart.find(item => item.id === product.id);
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            cart.push(product);
        }
        saveCart();
    }

    function updateQuantity(id, newQuantity) {
        const item = cart.find(item => item.id === id);
        if (item) {
            item.quantity = Math.max(1, newQuantity);
            if (item.quantity <= 0) {
                removeFromCart(id);
            } else {
                saveCart();
                renderCart();
            }
        }
    }

    function removeFromCart(id) {
        cart = cart.filter(item => item.id !== id);
        saveCart();
        renderCart();
    }

    function getTotal() {
        return cart.reduce((total, item) => total + (item.price * item.quantity), 0);
    }

    function saveCart() {
        localStorage.setItem('cart', JSON.stringify(cart));
    }

    // Render cart
    function renderCart() {
        const cartItemsContainer = document.getElementById('cart-items');
        const cartTotalContainer = document.getElementById('cart-total');
        const emptyCartContainer = document.getElementById('empty-cart');
        const totalAmountSpan = document.getElementById('total-amount');

        if (cart.length === 0) {
            cartItemsContainer.style.display = 'none';
            cartTotalContainer.style.display = 'none';
            emptyCartContainer.style.display = 'block';
            return;
        }

        emptyCartContainer.style.display = 'none';
        cartItemsContainer.style.display = 'block';
        cartTotalContainer.style.display = 'block';

        cartItemsContainer.innerHTML = cart.map(item => `
            <div class="cart-item" style="display:flex;align-items:center;padding:15px 0;border-bottom:1px solid #eee">
                <img src="${item.image}" alt="${item.name}" style="width:80px;height:80px;object-fit:cover;border-radius:8px;margin-right:15px">
                <div style="flex:1">
                    <h4 style="margin:0 0 5px 0;color:#000">${item.name}</h4>
                    <p style="margin:0;color:#666;font-size:14px">$${item.price}</p>
                </div>
                <div style="display:flex;align-items:center;gap:10px">
                    <button class="quantity-btn" data-id="${item.id}" data-action="decrease" style="width:35px;height:35px;border:1px solid #ddd;background:#f9f9f9;border-radius:4px;cursor:pointer;font-size:18px;font-weight:bold">-</button>
                    <span style="min-width:40px;text-align:center;font-size:16px;font-weight:bold;color:#000;padding:5px 10px;background:#fff;border:1px solid #ddd;border-radius:4px">${item.quantity}</span>
                    <button class="quantity-btn" data-id="${item.id}" data-action="increase" style="width:35px;height:35px;border:1px solid #ddd;background:#f9f9f9;border-radius:4px;cursor:pointer;font-size:18px;font-weight:bold">+</button>
                    <button class="remove-btn" data-id="${item.id}" style="margin-left:15px;padding:8px 12px;background:#ff3366;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:14px">Remove</button>
                </div>
            </div>
        `).join('');

        totalAmountSpan.textContent = getTotal().toFixed(2);
    }

    // Cart event listeners
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('quantity-btn')) {
            const id = e.target.getAttribute('data-id');
            const action = e.target.getAttribute('data-action');
            const item = cart.find(item => item.id === id);

            if (action === 'increase') {
                updateQuantity(id, item.quantity + 1);
            } else if (action === 'decrease') {
                updateQuantity(id, item.quantity - 1);
            }
        }

        if (e.target.classList.contains('remove-btn')) {
            const id = e.target.getAttribute('data-id');
            removeFromCart(id);
        }
    });

    // Checkout button
    document.getElementById('checkout-btn').addEventListener('click', function() {
        if (cart.length === 0) {
            alert('Your cart is empty!');
            return;
        }

        // First check if user is authenticated
        checkAuthentication().then(isAuthenticated => {
            if (!isAuthenticated) {
                const loginConfirmed = confirm('You must be logged in with Google to checkout. Would you like to login now?');
                if (loginConfirmed) {
                    window.location.href = '/oauth2/authorization/google';
                }
                return;
            }

            // Get user email from authentication status
            getUserInfo().then(userInfo => {
                const userEmail = userInfo.email || 'guest@example.com';

                // Prepare checkout data
                const checkoutData = {
                    cartItems: cart,
                    userEmail: userEmail
                };

                // Call checkout API
                fetch('/api/checkout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'same-origin', // Include authentication cookies
                    body: JSON.stringify(checkoutData)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.orderId) {
                        // Clear cart
                        cart = [];
                        saveCart();
                        renderCart();

                        // Redirect to payment page with order details
                        window.location.href = `/payment?orderId=${data.orderId}&totalAmount=${data.totalAmount}`;
                    } else {
                        alert('Checkout failed: ' + (data.error || 'Unknown error'));
                    }
                })
                .catch(error => {
                    console.error('Error during checkout:', error);
                    alert('Checkout failed. Please try again.');
                });
            });
        });
    });

    // Initialize cart display
    renderCart();

    // Check authentication status and update UI
    function checkAuthStatus() {
        fetch('/api/auth/status')
            .then(response => response.json())
            .then(data => {
                if (data.authenticated) {
                    // Show user profile section
                    document.getElementById('login-section').style.display = 'none';
                    document.getElementById('user-section').style.display = 'block';

                    // Update user info
                    document.getElementById('user-name').textContent = data.name || 'User';
                    document.getElementById('user-avatar').src = data.imageUrl || '/images/default-avatar.png';

                    console.log('User authenticated:', data.name, data.email);
                } else {
                    // Show login section
                    document.getElementById('login-section').style.display = 'block';
                    document.getElementById('user-section').style.display = 'none';

                    console.log('User not authenticated');
                }
            })
            .catch(error => {
                console.error('Error checking auth status:', error);
                // Default to showing login
                document.getElementById('login-section').style.display = 'block';
                document.getElementById('user-section').style.display = 'none';
            });
    }

    // Authentication helper functions
    function checkAuthentication() {
        return fetch('/api/auth/status')
            .then(response => response.json())
            .then(data => data.authenticated === true)
            .catch(error => {
                console.error('Error checking authentication:', error);
                return false;
            });
    }

    function getUserInfo() {
        return fetch('/api/auth/status')
            .then(response => response.json())
            .then(data => data)
            .catch(error => {
                console.error('Error getting user info:', error);
                return { email: 'guest@example.com' };
            });
    }

});




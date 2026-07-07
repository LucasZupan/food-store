import { getProducts } from "../../../services/product.service";
import { getCategories } from "../../../services/category.service";
import type { Product, CartItem } from "../../../types/product";
import type { ICategoria } from "../../../types/category";
import { logout } from "../../../utils/auth";
import { getSession } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { showToast } from "../../../utils/toast";

const CART_KEY = "cart";

const productsContainer = document.getElementById("productsContainer") as HTMLElement;
const categoryList = document.getElementById("categoryList") as HTMLUListElement;
const searchInput = document.getElementById("searchInput") as HTMLInputElement;
const sortSelect = document.getElementById("sortSelect") as HTMLSelectElement;
const productsInfo = document.getElementById("productsInfo") as HTMLParagraphElement;
const cartBadge = document.getElementById("cartBadge") as HTMLSpanElement | null;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;

const cartLink = document.getElementById("cartLink") as HTMLAnchorElement | null;
const ordersLink = document.getElementById("ordersLink") as HTMLAnchorElement | null;
const adminPanelLink = document.getElementById("adminPanelLink") as HTMLAnchorElement | null;

const productDetailModal = document.getElementById("productDetailModal") as HTMLDivElement;
const closeProductDetailButton = document.getElementById("closeProductDetailButton") as HTMLButtonElement;
const productDetailTitle = document.getElementById("productDetailTitle") as HTMLHeadingElement;
const productDetailContent = document.getElementById("productDetailContent") as HTMLDivElement;

let selectedCategory = "Todas";
let products: Product[] = [];
let categories: ICategoria[] = [];
let isAdmin = false;

const formatPrice = (price: number): string => {
  return `$${price.toLocaleString("es-AR")}`;
};

const getCart = (): CartItem[] => {
  if (isAdmin) {
    return [];
  }

  const cartStorage = localStorage.getItem(CART_KEY);
  return cartStorage ? JSON.parse(cartStorage) : [];
};

const saveCart = (cart: CartItem[]): void => {
  if (isAdmin) {
    return;
  }

  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

const updateCartBadge = (): void => {
  if (!cartBadge) return;

  if (isAdmin) {
    cartBadge.textContent = "0";
    cartBadge.style.display = "none";
    return;
  }

  const cart = getCart();
  const totalItems = cart.reduce((acc, item) => acc + item.quantity, 0);

  cartBadge.textContent = String(totalItems);
  cartBadge.style.display = "inline-block";
};

const addToCart = (productId: number): void => {
  if (isAdmin) {
    showToast("El administrador no puede realizar compras", "error");
    return;
  }

  const product = products.find((item) => item.id === productId);

  if (!product) {
    showToast("Producto no encontrado", "error");
    return;
  }

  if (!product.disponible || product.stock <= 0) {
    showToast("Este producto no está disponible", "error");
    return;
  }

  const cart = getCart();
  const existingItem = cart.find((item) => item.id === product.id);

  if (existingItem) {
    if (existingItem.quantity >= product.stock) {
      showToast("No hay más stock disponible para este producto", "error");
      return;
    }

    existingItem.quantity += 1;
  } else {
    cart.push({
      ...product,
      quantity: 1,
    });
  }

  saveCart(cart);
  updateCartBadge();
  renderProducts();

  if (!existingItem) {
    showToast(`${product.nombre} agregado al carrito`);
  }
};

const increaseQuantity = (productId: number): void => {
  if (isAdmin) {
    return;
  }

  const cart = getCart();
  const item = cart.find((product) => product.id === productId);

  if (!item) return;

  if (item.quantity >= item.stock) {
    showToast("No hay más stock disponible para este producto", "error");
    return;
  }

  item.quantity += 1;
  saveCart(cart);
  updateCartBadge();
  renderProducts();
};

const decreaseQuantity = (productId: number): void => {
  if (isAdmin) {
    return;
  }

  let cart = getCart();
  const item = cart.find((product) => product.id === productId);

  if (!item) return;

  if (item.quantity === 1) {
    cart = cart.filter((product) => product.id !== productId);
    showToast(`${item.nombre} eliminado del carrito`, "error");
  } else {
    item.quantity -= 1;
  }

  saveCart(cart);
  updateCartBadge();
  renderProducts();
};

const getFilteredProducts = (): Product[] => {
  const searchValue = searchInput.value.trim().toLowerCase();
  const sortValue = sortSelect.value;

  let filteredProducts = [...products];

  if (selectedCategory !== "Todas") {
    filteredProducts = filteredProducts.filter(
      (product) => product.categoria.nombre === selectedCategory
    );
  }

  if (searchValue) {
    filteredProducts = filteredProducts.filter((product) =>
      product.nombre.toLowerCase().includes(searchValue)
    );
  }

  if (sortValue === "name-asc") {
    filteredProducts.sort((a, b) => a.nombre.localeCompare(b.nombre));
  }

  if (sortValue === "name-desc") {
    filteredProducts.sort((a, b) => b.nombre.localeCompare(a.nombre));
  }

  if (sortValue === "price-asc") {
    filteredProducts.sort((a, b) => a.precio - b.precio);
  }

  if (sortValue === "price-desc") {
    filteredProducts.sort((a, b) => b.precio - a.precio);
  }

  return filteredProducts;
};

const getImageSrc = (image: string): string => {
  if (!image) {
    return "/assets/default-product.png";
  }

  if (image.startsWith("http")) {
    return image;
  }

  return `/assets/${image}`;
};

const openProductDetail = (product: Product): void => {
  productDetailTitle.textContent = product.nombre;

  productDetailContent.innerHTML = `
    <div class="product-detail-content">
      <img src="${getImageSrc(product.imagen)}" alt="${product.nombre}" />

      <div class="product-detail-info">
        <h3>${product.nombre}</h3>

        <span class="badge ${product.disponible ? "badge-success" : "badge-danger"}">
          ${product.disponible ? "Disponible" : "No disponible"}
        </span>

        <p><strong>Categoría:</strong> ${product.categoria.nombre}</p>
        <p>${product.descripcion ?? ""}</p>
        <p><strong>Stock:</strong> ${product.stock}</p>

        <strong class="product-price">${formatPrice(product.precio)}</strong>

        ${
          isAdmin
            ? `<button class="primary-button" disabled>Solo vista</button>`
            : product.disponible && product.stock > 0
              ? getCart().find((item) => item.id === product.id)
                ? `
                  <div class="quantity-controls product-quantity-controls">
                    <button id="decreaseFromDetailButton">-</button>
                    <strong>${getCart().find((item) => item.id === product.id)?.quantity}</strong>
                    <button
                      id="increaseFromDetailButton"
                      ${getCart().find((item) => item.id === product.id)!.quantity >= product.stock ? "disabled" : ""}
                    >
                      +
                    </button>
                  </div>
                `
                : `<button id="addFromDetailButton" class="primary-button">Agregar al carrito</button>`
              : `<button class="primary-button" disabled>No disponible</button>`
        }
      </div>
    </div>
  `;

  productDetailModal.classList.add("open");

  const addFromDetailButton = document.getElementById("addFromDetailButton");
  const increaseFromDetailButton = document.getElementById("increaseFromDetailButton");
  const decreaseFromDetailButton = document.getElementById("decreaseFromDetailButton");

  increaseFromDetailButton?.addEventListener("click", () => {
    increaseQuantity(product.id);
    openProductDetail(product);
  });

  decreaseFromDetailButton?.addEventListener("click", () => {
    decreaseQuantity(product.id);
    openProductDetail(product);
  });

    addFromDetailButton?.addEventListener("click", () => {
      addToCart(product.id);
      openProductDetail(product);
    });
  };

const closeProductDetail = (): void => {
  productDetailModal.classList.remove("open");
};


const renderProducts = (): void => {
  const filteredProducts = getFilteredProducts();
  const cart = getCart();

  productsContainer.innerHTML = "";
  productsInfo.textContent = `${filteredProducts.length} producto(s) encontrado(s)`;

  if (filteredProducts.length === 0) {
    productsContainer.innerHTML = `
      <p class="empty-message">No se encontraron productos.</p>
    `;
    return;
  }

  filteredProducts.forEach((product) => {
    const article = document.createElement("article");
    const cartItem = isAdmin
      ? null
      : cart.find((item) => item.id === product.id);

    article.className = "product-card";

    article.innerHTML = `
      <img src="${getImageSrc(product.imagen)}" alt="${product.nombre}" />

      <div class="product-card-body">
        <h3>${product.nombre}</h3>
        <p>${product.descripcion ?? ""}</p>
        <small>${product.categoria.nombre}</small>
      </div>

      <div class="product-card-footer">
        <strong class="product-price">${formatPrice(product.precio)}</strong>

        <span class="badge ${product.disponible ? "badge-success" : "badge-danger"}">
          ${product.disponible ? "Disponible" : "No disponible"}
        </span>

        <div class="product-card-actions">
          ${
            cartItem
              ? `
                <div class="quantity-controls product-quantity-controls">
                  <button data-action="decrease-home" data-product-id="${product.id}">-</button>
                  <strong>${cartItem.quantity}</strong>
                  <button
                    data-action="increase-home"
                    data-product-id="${product.id}"
                    ${cartItem.quantity >= product.stock ? "disabled" : ""}
                  >
                    +
                  </button>
                </div>
              `
              : `
                <button
                  ${
                    isAdmin ||
                    !product.disponible ||
                    product.stock <= 0
                      ? "disabled"
                      : ""
                  }
                  data-action="add-home"
                  data-product-id="${product.id}"
                >
                  ${isAdmin ? "Solo vista" : "Agregar al carrito"}
                </button>
              `
          }
        </div>
      </div>
    `;

    productsContainer.appendChild(article);
    article.addEventListener("click", () => {
    openProductDetail(product);
});
  });

  const buttons = document.querySelectorAll<HTMLButtonElement>("[data-action]");

  buttons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.stopPropagation();
      const productId = Number(button.dataset.productId);
      const action = button.dataset.action;

      if (action === "add-home") addToCart(productId);
      if (action === "increase-home") increaseQuantity(productId);
      if (action === "decrease-home") decreaseQuantity(productId);
    });
  });
};

const renderCategories = (): void => {
  const categoryNames = ["Todas", ...categories.map((category) => category.nombre)];

  categoryList.innerHTML = "";

  categoryNames.forEach((category) => {
    const li = document.createElement("li");
    const button = document.createElement("button");

    button.textContent = category;
    button.className = `category-button ${
      category === selectedCategory ? "active" : ""
    }`;

    button.addEventListener("click", () => {
      selectedCategory = category;
      renderCategories();
      renderProducts();
    });

    li.appendChild(button);
    categoryList.appendChild(li);
  });
};

const loadData = async (): Promise<void> => {
  try {
    productsContainer.innerHTML = `<p class="empty-message">Cargando productos...</p>`;

    const [productsResponse, categoriesResponse] = await Promise.all([
      getProducts(),
      getCategories(),
    ]);

    products = productsResponse;
    categories = categoriesResponse;

    renderCategories();
    renderProducts();
    updateCartBadge();
    } catch (error) {
      console.error("ERROR LOAD DATA:", error);

      productsContainer.innerHTML = `
        <p class="empty-message">No se pudieron cargar los productos.</p>
      `;

      showToast("Error al cargar productos", "error");
    }
  };

const initPage = (): void => {
  const session = getSession();

  if (!session) {
    navigate("/src/pages/auth/login/login.html");
    return;
  }

  isAdmin = session.rol === "ADMIN";

  if (isAdmin) {
    if (cartLink) cartLink.style.display = "none";
    if (ordersLink) ordersLink.style.display = "none";
    if (adminPanelLink) adminPanelLink.style.display = "inline-block";
  } else {
    if (adminPanelLink) adminPanelLink.style.display = "none";
  }

  searchInput.addEventListener("input", renderProducts);
  sortSelect.addEventListener("change", renderProducts);

  logoutButton.addEventListener("click", () => {
    logout();
  });

  closeProductDetailButton.addEventListener("click", closeProductDetail);

  productDetailModal.addEventListener("click", (e) => {
    if (e.target === productDetailModal) {
      closeProductDetail();
    }
  });
  loadData();
};

initPage();
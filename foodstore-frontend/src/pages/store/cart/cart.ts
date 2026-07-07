import type { CartItem } from "../../../types/product";
import { createOrder } from "../../../services/order.service";
import { logout } from "../../../utils/auth";
import { getSession } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { showToast } from "../../../utils/toast";


const CART_KEY = "cart";

const cartContent = document.getElementById("cartContent") as HTMLDivElement;
const cartSubtotal = document.getElementById("cartSubtotal") as HTMLElement;
const cartTotal = document.getElementById("cartTotal") as HTMLElement;
const clearCartButton = document.getElementById("clearCartButton") as HTMLButtonElement;
const checkoutButton = document.getElementById("checkoutButton") as HTMLButtonElement;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;
const checkoutModal = document.getElementById("checkoutModal") as HTMLDivElement;
const closeCheckoutModalButton = document.getElementById("closeCheckoutModalButton") as HTMLButtonElement;
const cancelCheckoutButton = document.getElementById("cancelCheckoutButton") as HTMLButtonElement;
const confirmCheckoutButton = document.getElementById("confirmCheckoutButton") as HTMLButtonElement;
const checkoutModalTotal = document.getElementById("checkoutModalTotal") as HTMLElement;
const paymentMethod = document.getElementById("paymentMethod") as HTMLSelectElement;

const formatPrice = (price: number): string => {
  return `$${price.toLocaleString("es-AR")}`;
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

const getCart = (): CartItem[] => {
  const cartStorage = localStorage.getItem(CART_KEY);
  return cartStorage ? JSON.parse(cartStorage) : [];
};

const saveCart = (cart: CartItem[]): void => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

const calculateTotal = (cart: CartItem[]): number => {
  return cart.reduce((acc, item) => acc + item.precio * item.quantity, 0);
};

const increaseQuantity = (productId: number): void => {
  const cart = getCart();
  const item = cart.find((product) => product.id === productId);

  if (!item) return;

  if (item.quantity >= item.stock) {
    showToast("No hay más stock disponible para este producto", "error");
    return;
  }

  item.quantity += 1;
  saveCart(cart);
  renderCart();
};

const decreaseQuantity = (productId: number): void => {
  let cart = getCart();
  const item = cart.find((product) => product.id === productId);

  if (!item) return;

  if (item.quantity === 1) {
    cart = cart.filter((product) => product.id !== productId);
  } else {
    item.quantity -= 1;
  }

  saveCart(cart);
  renderCart();
};

const removeItem = (productId: number): void => {
  const cart = getCart().filter((product) => product.id !== productId);
  saveCart(cart);
  renderCart();
};

const clearCart = (): void => {
  localStorage.removeItem(CART_KEY);
  renderCart();
};

const openCheckoutModal = (): void => {
  const cart = getCart();

  if (cart.length === 0) {
    showToast("El carrito está vacío", "error");
    return;
  }

  checkoutModalTotal.textContent = formatPrice(calculateTotal(cart));
  checkoutModal.classList.add("open");
};

const closeCheckoutModal = (): void => {
  checkoutModal.classList.remove("open");
};

const confirmOrder = async (): Promise<void> => {
  const cart = getCart();

  if (cart.length === 0) {
    showToast("El carrito está vacío", "error");
    return;
  }

  try {
    confirmCheckoutButton.disabled = true;
    confirmCheckoutButton.textContent = "Confirmando...";

    await createOrder({
      estado: "PENDIENTE",
      formaPago: paymentMethod.value as "TARJETA" | "TRANSFERENCIA" | "EFECTIVO",
      detallePedido: cart.map((item) => ({
        idProducto: item.id,
        cantidad: item.quantity,
      })),
    });

    closeCheckoutModal();
    clearCart();
    showToast("Pedido creado correctamente", "success");

    navigate("/src/pages/client/orders/orders.html");
  } catch (error) {
    const message =
      error instanceof Error ? error.message : "Error al confirmar el pedido";

    showToast(message, "error");
  } finally {
    confirmCheckoutButton.disabled = false;
    confirmCheckoutButton.textContent = "Confirmar pedido";
  }
};

const renderCart = (): void => {
  const cart = getCart();
  const total = calculateTotal(cart);

  cartContent.innerHTML = "";
  cartSubtotal.textContent = formatPrice(total);
  cartTotal.textContent = formatPrice(total);

  if (cart.length === 0) {
    cartContent.innerHTML = `
      <p class="empty-message">Tu carrito está vacío.</p>
      <a class="primary-button" href="../home/home.html">Volver a la tienda</a>
    `;

    clearCartButton.disabled = true;
    checkoutButton.disabled = true;
    return;
  }

  clearCartButton.disabled = false;
  checkoutButton.disabled = false;

  cart.forEach((item) => {
    const subtotal = item.precio * item.quantity;

    const article = document.createElement("article");
    article.className = "cart-item";

    article.innerHTML = `
      <img src="${getImageSrc(item.imagen)}" alt="${item.nombre}" />

      <div>
        <h3>${item.nombre}</h3>
        <p>Precio unitario: ${formatPrice(item.precio)}</p>
      </div>

      <div class="quantity-controls">
        <button data-action="decrease" data-product-id="${item.id}">-</button>
        <strong>${item.quantity}</strong>
        <button
          data-action="increase"
          data-product-id="${item.id}"
          ${item.quantity >= item.stock ? "disabled" : ""}
        >
          +
        </button>
      </div>

      <strong>${formatPrice(subtotal)}</strong>

      <button class="delete-button" data-action="remove" data-product-id="${item.id}">
        Eliminar
      </button>
    `;

    cartContent.appendChild(article);
  });

  const buttons = document.querySelectorAll<HTMLButtonElement>("[data-action]");

  buttons.forEach((button) => {
    button.addEventListener("click", () => {
      const productId = Number(button.dataset.productId);
      const action = button.dataset.action;

      if (action === "increase") increaseQuantity(productId);
      if (action === "decrease") decreaseQuantity(productId);
      if (action === "remove") removeItem(productId);
    });
  });
};

const initPage = (): void => {
  const session = getSession();

  if (!session) {
    navigate("/src/pages/auth/login/login.html");
    return;
  }

    if (session.rol === "ADMIN") {
    navigate("/src/pages/admin/home/home.html");
    return;
  }

  renderCart();

  clearCartButton.addEventListener("click", () => {
    clearCart();
    showToast("Carrito vaciado correctamente");
  });

  checkoutButton.addEventListener("click", () => {
  openCheckoutModal();
  });

  confirmCheckoutButton.addEventListener("click", () => {
    confirmOrder();
  });

  closeCheckoutModalButton.addEventListener("click", () => {
    closeCheckoutModal();
  });

  cancelCheckoutButton.addEventListener("click", () => {
    closeCheckoutModal();
  });

  checkoutModal.addEventListener("click", (e) => {
    if (e.target === checkoutModal) {
      closeCheckoutModal();
    }
  });

  logoutButton.addEventListener("click", () => {
    logout();
  });
};

initPage();
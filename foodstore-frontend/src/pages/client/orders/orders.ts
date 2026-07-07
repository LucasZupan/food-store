import { logout } from "../../../utils/auth";
import { getSession, getToken } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { showToast } from "../../../utils/toast";
import { API_URL } from "../../../config/api";

interface ProductoPedido {
  id: number;
  nombre: string;
  precio: number;
}

interface DetallePedido {
  id: number;
  cantidad: number;
  subtotal: number;
  producto: ProductoPedido;
}

interface Pedido {
  id: number;
  fecha: string;
  estado: string;
  total: number;
  formaPago: string;
  idUsuario: number;
  detalles: DetallePedido[];
}

const ordersContent = document.getElementById("ordersContent") as HTMLDivElement;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;

const formatPrice = (price: number): string => {
  return `$${price.toLocaleString("es-AR")}`;
};

const formatDate = (date: string): string => {
  return new Date(date).toLocaleDateString("es-AR");
};

const getStatusClass = (estado: string): string => {
  return `client-order-status client-order-status-${estado.toLowerCase()}`;
};

const getMyOrders = async (): Promise<Pedido[]> => {
  const token = getToken();

  const response = await fetch(`${API_URL}/pedidos/mis-pedidos`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Error al obtener pedidos");
  }

  return response.json();
};

const renderOrders = (orders: Pedido[]): void => {
  ordersContent.innerHTML = "";

  if (orders.length === 0) {
    ordersContent.innerHTML = `
      <article class="client-order-card">
        <p class="empty-message">Todavía no tenés pedidos.</p>
        <a class="primary-button" href="../../store/home/home.html">Volver a la tienda</a>
      </article>
    `;
    return;
  }

  [...orders]
  .sort((a, b) => b.id - a.id)
  .forEach((order) => {
      const article = document.createElement("article");
      article.className = "client-order-card";

      const productos = order.detalles
        .map(
          (detalle) => `
            <div class="client-order-product">
              <span>${detalle.cantidad}x ${detalle.producto.nombre}</span>
              <strong>${formatPrice(detalle.subtotal)}</strong>
            </div>
          `
        )
        .join("");

      article.innerHTML = `
        <div class="client-order-header">
          <div>
            <h3>Pedido #${order.id}</h3>
            <p>Fecha: ${formatDate(order.fecha)}</p>
            <p>Forma de pago: ${order.formaPago}</p>
          </div>

          <div class="client-order-summary">
            <span class="${getStatusClass(order.estado)}">
              ${order.estado}
            </span>
            <strong>${formatPrice(order.total)}</strong>
          </div>
        </div>

        <div class="client-order-products">
          ${productos}
        </div>
      `;

      ordersContent.appendChild(article);
    });
};

const loadOrders = async (): Promise<void> => {
  try {
    ordersContent.innerHTML = `
      <article class="client-order-card">
        <p class="empty-message">Cargando pedidos...</p>
      </article>
    `;

    const orders = await getMyOrders();

    renderOrders(orders);
  } catch (error) {
    ordersContent.innerHTML = `
      <article class="client-order-card">
        <p class="empty-message">No se pudieron cargar los pedidos.</p>
      </article>
    `;
    showToast("Error al cargar pedidos", "error");
  }
};

const initPage = (): void => {
  const session = getSession();

  if (!session) {
    navigate("/src/pages/auth/login/login.html");
    return;
  }

  logoutButton.addEventListener("click", () => {
    logout();
  });

  loadOrders();
};

initPage();
import {
  getAllOrders,
  updateOrder,
  deleteOrder,
  type Pedido,
} from "../../../services/order.service";

import { checkAuthUser, logout } from "../../../utils/auth";
import { showToast } from "../../../utils/toast";

const ordersContainer = document.getElementById("ordersContainer") as HTMLDivElement;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;
const estadoFilter = document.getElementById("estadoFilter") as HTMLSelectElement;

let orders: Pedido[] = [];

const estados = [
  "PENDIENTE",
  "CONFIRMADO",
  "EN_PREPARACION",
  "ENVIADO",
  "ENTREGADO",
  "TERMINADO",
  "CANCELADO",
];

const formasPago = [
  "TARJETA",
  "TRANSFERENCIA",
  "EFECTIVO",
];

const formatPrice = (price: number): string => {
  return `$${price.toLocaleString("es-AR")}`;
};

const formatDate = (date: string): string => {
  return new Date(date).toLocaleDateString("es-AR");
};

const getStatusClass = (estado: string): string => {
  return `order-status order-status-${estado.toLowerCase()}`;
};

const getFilteredOrders = (): Pedido[] => {
  const estado = estadoFilter.value;

  if (estado === "TODOS") {
    return orders;
  }

  return orders.filter((order) => order.estado === estado);
};

const renderOrders = (): void => {
  const filteredOrders = getFilteredOrders();

  ordersContainer.innerHTML = "";

  if (filteredOrders.length === 0) {
    ordersContainer.innerHTML = `
      <div class="admin-panel">
        <p>No hay pedidos para mostrar.</p>
      </div>
    `;
    return;
  }

  filteredOrders.forEach((order) => {
    const article = document.createElement("article");
    article.className = "admin-order-card";

    const detalles = order.detalles
      .map(
        (detalle) =>
          `<div>${detalle.cantidad}x ${detalle.producto.nombre} — ${formatPrice(detalle.subtotal)}</div>`
      )
      .join("");

    const estadoOptions = estados
      .map(
        (estado) =>
          `<option value="${estado}" ${estado === order.estado ? "selected" : ""}>
            ${estado}
          </option>`
      )
      .join("");

    const formaPagoOptions = formasPago
      .map(
        (formaPago) =>
          `<option value="${formaPago}" ${formaPago === order.formaPago ? "selected" : ""}>
            ${formaPago}
          </option>`
      )
      .join("");

    article.innerHTML = `
      <div class="admin-order-header">
        <div class="admin-order-title">
          <h3>Pedido #${order.id}</h3>
          <p>Usuario ID: ${order.idUsuario}</p>
          <p>Fecha: ${formatDate(order.fecha)}</p>
          <span class="${getStatusClass(order.estado)}">${order.estado}</span>
        </div>

        <div class="admin-order-total">
          <span>Total</span>
          <strong>${formatPrice(order.total)}</strong>
          <small>${order.formaPago}</small>
        </div>
      </div>

      <div class="admin-order-details">
        ${detalles}
      </div>

      <div class="admin-order-actions">
        <div>
          <label>Estado</label>
          <select data-action="estado" data-order-id="${order.id}">
            ${estadoOptions}
          </select>
        </div>

        <div>
          <label>Forma de pago</label>
          <select data-action="formaPago" data-order-id="${order.id}">
            ${formaPagoOptions}
          </select>
        </div>

        <button
          class="admin-action-button admin-edit-button"
          data-action="update"
          data-order-id="${order.id}"
        >
          Actualizar
        </button>

        <button
          class="admin-action-button admin-delete-button"
          data-action="delete"
          data-order-id="${order.id}"
        >
          Eliminar
        </button>
      </div>
    `;

    ordersContainer.appendChild(article);
  });

  document
    .querySelectorAll<HTMLButtonElement>("[data-action='update']")
    .forEach((button) => {
      button.addEventListener("click", async () => {
        const id = Number(button.dataset.orderId);

        const estadoSelect = document.querySelector<HTMLSelectElement>(
          `select[data-action="estado"][data-order-id="${id}"]`
        );

        const formaPagoSelect = document.querySelector<HTMLSelectElement>(
          `select[data-action="formaPago"][data-order-id="${id}"]`
        );

        if (!estadoSelect || !formaPagoSelect) return;

        try {
          await updateOrder(id, {
            estado: estadoSelect.value,
            formaPago: formaPagoSelect.value,
          });

          showToast("Pedido actualizado", "success");
          await loadOrders();
        } catch (error) {
          const message =
            error instanceof Error ? error.message : "No se pudo actualizar el pedido";

          showToast(message, "error");
        }
      });
    });

  document
    .querySelectorAll<HTMLButtonElement>("[data-action='delete']")
    .forEach((button) => {
      button.addEventListener("click", async () => {
        const id = Number(button.dataset.orderId);

        try {
          await deleteOrder(id);
          showToast("Pedido eliminado", "success");
          await loadOrders();
        } catch (error) {
          const message =
            error instanceof Error ? error.message : "No se pudo eliminar el pedido";

          showToast(message, "error");
        }
      });
    });
};

const loadOrders = async (): Promise<void> => {
  try {
    ordersContainer.innerHTML = `
      <div class="admin-panel">
        <p>Cargando pedidos...</p>
      </div>
    `;

    orders = await getAllOrders();

    orders.sort(
      (a, b) =>
        new Date(b.fecha).getTime() - new Date(a.fecha).getTime()
    );

    renderOrders();
  } catch {
    ordersContainer.innerHTML = `
      <div class="admin-panel">
        <p>No se pudieron cargar los pedidos.</p>
      </div>
    `;

    showToast("Error al cargar pedidos", "error");
  }
};

const initPage = (): void => {
  checkAuthUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "ADMIN"
  );

  logoutButton.addEventListener("click", logout);

  estadoFilter.addEventListener("change", renderOrders);

  loadOrders();
};

initPage();
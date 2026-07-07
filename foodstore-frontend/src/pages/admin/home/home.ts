import { getProducts } from "../../../services/product.service";
import { getCategories } from "../../../services/category.service";
import { getAllOrders } from "../../../services/order.service";
import { checkAuthUser, logout } from "../../../utils/auth";
import { getSession } from "../../../utils/localStorage";
import { showToast } from "../../../utils/toast";

const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;
const adminStats = document.getElementById("adminStats") as HTMLDivElement;
const adminSummary = document.getElementById("adminSummary") as HTMLParagraphElement;
const adminEmail = document.getElementById("adminEmail") as HTMLSpanElement;

const renderStats = async (): Promise<void> => {
  try {
    adminStats.innerHTML = `<p>Cargando dashboard...</p>`;

    const [products, categories, orders] = await Promise.all([
      getProducts(),
      getCategories(),
      getAllOrders(),
    ]);

    const productosDisponibles = products.filter((product) => product.disponible).length;

    adminStats.innerHTML = `
      <article class="admin-stat-card card-purple">
        <h3>📁 Categorías</h3>
        <strong>${categories.length}</strong>
        <a href="../categories/categories.html">Gestionar</a>
      </article>

      <article class="admin-stat-card card-pink">
        <h3>🍔 Productos</h3>
        <strong>${products.length}</strong>
        <a href="../products/products.html">Gestionar</a>
      </article>

      <article class="admin-stat-card card-cyan">
        <h3>📦 Pedidos</h3>
        <strong>${orders.length}</strong>
        <a href="../orders/orders.html">Gestionar</a>
      </article>

      <article class="admin-stat-card card-green">
        <h3>✅ Disponibles</h3>
        <strong>${productosDisponibles}</strong>
        <span>Productos activos</span>
      </article>
    `;

    adminSummary.textContent =
      `Actualmente hay ${categories.length} categorías, ${products.length} productos, ` +
      `${productosDisponibles} productos disponibles y ${orders.length} pedidos registrados.`;
  } catch (error) {
    adminStats.innerHTML = `<p>No se pudo cargar el dashboard.</p>`;
    adminSummary.textContent = "No se pudieron cargar las estadísticas.";
    showToast("Error al cargar dashboard", "error");
  }
};

const initPage = (): void => {
  checkAuthUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "ADMIN"
  );

  const session = getSession();

  if (session) {
    adminEmail.textContent = session.email;
  }

  logoutButton.addEventListener("click", () => {
    logout();
  });

  renderStats();
};

initPage();
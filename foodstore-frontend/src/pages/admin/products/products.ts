import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../../../services/product.service";

import { getCategories } from "../../../services/category.service";
import type { Product } from "../../../types/product";
import type { ICategoria } from "../../../types/category";
import { checkAuthUser, logout } from "../../../utils/auth";
import { showToast } from "../../../utils/toast";

const form = document.getElementById("productForm") as HTMLFormElement;

const inputNombre = document.getElementById("nombre") as HTMLInputElement;
const inputPrecio = document.getElementById("precio") as HTMLInputElement;
const inputDescripcion = document.getElementById("descripcion") as HTMLTextAreaElement;
const inputStock = document.getElementById("stock") as HTMLInputElement;
const inputImagen = document.getElementById("imagen") as HTMLInputElement;
const selectCategoria = document.getElementById("categoria") as HTMLSelectElement;
const inputDisponible = document.getElementById("disponible") as HTMLInputElement;

const tableBody = document.getElementById("productsTableBody") as HTMLTableSectionElement;
const logoutButton = document.getElementById("logoutButton") as HTMLButtonElement;

const modal = document.getElementById("productModal") as HTMLDivElement;
const modalTitle = document.getElementById("modalTitle") as HTMLHeadingElement;
const openModalButton = document.getElementById("openModalButton") as HTMLButtonElement;
const closeModalButton = document.getElementById("closeModalButton") as HTMLButtonElement;
const availabilityFilter = document.getElementById("availabilityFilter") as HTMLSelectElement;

let products: Product[] = [];
let categories: ICategoria[] = [];
let editingProductId: number | null = null;

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

const openModal = (): void => {
  modal.classList.add("open");
};

const closeModal = (): void => {
  modal.classList.remove("open");
};

const resetForm = (): void => {
  form.reset();
  inputDisponible.checked = true;
  editingProductId = null;
  modalTitle.textContent = "Nuevo Producto";
};

const renderCategoryOptions = (): void => {
  selectCategoria.innerHTML = `<option value="">Seleccionar categoría</option>`;

  categories.forEach((category) => {
    const option = document.createElement("option");

    option.value = String(category.id);
    option.textContent = category.nombre;

    selectCategoria.appendChild(option);
  });
};

const fillFormForEdit = (product: Product): void => {
  editingProductId = product.id;

  inputNombre.value = product.nombre;
  inputPrecio.value = String(product.precio);
  inputDescripcion.value = product.descripcion ?? "";
  inputStock.value = String(product.stock);
  inputImagen.value = product.imagen ?? "";
  selectCategoria.value = String(product.categoria.id);
  inputDisponible.checked = product.disponible;

  modalTitle.textContent = "Editar Producto";
  openModal();
};

const getFilteredProducts = (): Product[] => {
  const filterValue = availabilityFilter.value;

  if (filterValue === "DISPONIBLES") {
    return products.filter((product) => product.disponible);
  }

  if (filterValue === "NO_DISPONIBLES") {
    return products.filter((product) => !product.disponible);
  }

  return products;
};

const renderProducts = (): void => {
  tableBody.innerHTML = "";

  const filteredProducts = getFilteredProducts();

  if (filteredProducts.length === 0) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="7">No hay productos cargados.</td>
      </tr>
    `;
    return;
  }

  filteredProducts.forEach((product) => {
    const row = document.createElement("tr");

    row.innerHTML = `
      <td>
        <img
          class="admin-product-image"
          src="${getImageSrc(product.imagen)}"
          alt="${product.nombre}"
        >
      </td>

      <td>
        <strong>${product.nombre}</strong>
        <br>
        <small>${product.descripcion}</small>
      </td>

      <td>${product.categoria.nombre}</td>

      <td>${formatPrice(product.precio)}</td>

      <td>${product.stock}</td>

      <td>
        <span class="admin-status-badge ${
          product.disponible ? "admin-status-active" : "admin-status-inactive"
        }">
          ${product.disponible ? "Disponible" : "No disponible"}
        </span>
      </td>

      <td>
        <button
          class="admin-action-button admin-edit-button"
          data-action="edit"
          data-id="${product.id}"
        >
          Editar
        </button>

        <button
          class="admin-action-button admin-delete-button"
          data-action="delete"
          data-id="${product.id}"
        >
          Eliminar
        </button>
      </td>
    `;

    tableBody.appendChild(row);
  });

  document.querySelectorAll<HTMLButtonElement>("[data-action]").forEach((button) => {
    button.addEventListener("click", async () => {
      const id = Number(button.dataset.id);
      const action = button.dataset.action;

      if (action === "edit") {
        const product = products.find((item) => item.id === id);

        if (!product) {
          showToast("Producto no encontrado", "error");
          return;
        }

        fillFormForEdit(product);
      }

      if (action === "delete") {
        try {
          await deleteProduct(id);
          showToast("Producto eliminado", "success");
          await loadData();
        } catch (error) {
          const message =
            error instanceof Error ? error.message : "No se pudo eliminar el producto";

          showToast(message, "error");
        }
      }
    });
  });
};

const loadData = async (): Promise<void> => {
  try {
    tableBody.innerHTML = `
      <tr>
        <td colspan="7">Cargando productos...</td>
      </tr>
    `;

    const [productsResponse, categoriesResponse] = await Promise.all([
      getProducts(),
      getCategories(),
    ]);

    products = productsResponse;
    categories = categoriesResponse;

    renderCategoryOptions();
    renderProducts();
  } catch {
    tableBody.innerHTML = `
      <tr>
        <td colspan="7">No se pudieron cargar los productos.</td>
      </tr>
    `;

    showToast("Error al cargar productos", "error");
  }
};

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    nombre: inputNombre.value.trim(),
    precio: Number(inputPrecio.value),
    descripcion: inputDescripcion.value.trim(),
    stock: Number(inputStock.value),
    imagen: inputImagen.value.trim(),
    disponible: inputDisponible.checked,
    idCategoria: Number(selectCategoria.value),
  };

  try {
    if (editingProductId) {
      await updateProduct(editingProductId, data);
      showToast("Producto actualizado", "success");
    } else {
      await createProduct(data);
      showToast("Producto creado", "success");
    }

    resetForm();
    closeModal();
    await loadData();
  } catch (error) {
    const message =
      error instanceof Error ? error.message : "No se pudo guardar el producto";

    showToast(message, "error");
  }
});

openModalButton.addEventListener("click", () => {
  resetForm();
  openModal();
});

closeModalButton.addEventListener("click", () => {
  resetForm();
  closeModal();
});

modal.addEventListener("click", (e) => {
  if (e.target === modal) {
    resetForm();
    closeModal();
  }
});

const initPage = (): void => {
  checkAuthUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "ADMIN"
  );

  logoutButton.addEventListener("click", logout);
  availabilityFilter.addEventListener("change", renderProducts);

  loadData();
};



initPage();